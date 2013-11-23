package ge.amigo.neuro.console.client.math.neuro;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;
import ge.amigo.neuro.console.client.math.function.Function;
import ge.amigo.neuro.console.client.math.function.SigmoidFunction;

import java.util.ArrayList;
import java.util.List;

/** 
 * ორშრიანი უკუგავრცელების ნეირონული ქსელი 
 */
public class BackpropNetwork {

	// სწავლების სიჩქარე 0 < learningRate < 1
	private double learningRate = 0.2;
	
	// სწავლების მომენტი 0 < learningMomentum < 1
	private double learningMomentum = 0.1;
	
	// ნეირონული ქსელის შესასვლელზე არსებული ვექტორი
	private List<Double> inputs;

	// შრეები ბოლოს. გარდა ყველა ფარული შრეა
	private List<List<ScalarNeuron>> layers;

	// შესასვლელზე არსებული ვექტორისთვის ნეირონული ქსელის სასურველი გამომავალი მნიშვნელობები
	private List<Double> desiredOutputs;

	// სწავლების იტერაციის ნომერი
	private int learnStepCounter = 0;
	
	// ფარული შრის ნეირონების ძველი წონების შესანახი ობიექტი
	private List<List<List<Double>>> oldWeights;

	// ლოკალური გრადიენტის შესანახი ობიექტი
	private List<List<Double>> deltas;

	public BackpropNetwork () {
		
	}
	
	/** 
	 * ნეირონული ქსელის ინიციალიზაცია
	 */
	public void init(int inputCount, List<Integer> layerCounts, Function activationFunction) {
		layers = new ArrayList<List<ScalarNeuron>>();
		for (int l = 0; l < layerCounts.size(); l++) {
			List<ScalarNeuron> layer = new ArrayList<ScalarNeuron>();
			for (int i = 0; i < layerCounts.get(l); i++) {
				ScalarNeuron neuron = new ScalarNeuron();
				neuron.setActivationFunction(activationFunction);
				neuron.setInputs(VectorUtils.getVector(l == 0 ? inputCount : layerCounts.get(l - 1), null));
				layer.add(neuron);
			}
			layers.add(layer);
		}
		
		// გავანულოთ ძველი წონების მასივი
		resetOldWeights();
		// დავაყენოთ შემთხვევითი წონები და ბაზისი
		setWeightsAndBias(null);
	}
	
	public void init(int inputCount, List<Integer> layerCounts) {
		init(inputCount, layerCounts, new SigmoidFunction());
	}

	/**
	 * საწყისი ვექტორის შეშვება ნეირონულ ქსელში და საბოლოო გამოსავალის მიღება.
	 * @param inputs საწყისი ვექტორი
	 * @return გამომავალი ვექტორი
	 */
	public List<Double> applyInputs(List<Double> inputs) {
		setInputs(inputs);
		for (int l = 0; l < layers.size(); l++) {
			List<ScalarNeuron> layer = layers.get(l);
			for (int j = 0; j < layer.size(); j++) {
				ScalarNeuron neuron = layer.get(j);
				neuron.applyInputs(l == 0 ? inputs : getLayerOuts(l - 1));
			}
		}
		return getOut();
	}
	
	/**
	 * სწავლება სამაგალითო წყვილის საფუძველზე.
	 * @param x სამაგალითო მონაცემები
	 * @param d ქსელის სასურველი გამომავალი მნიშვნელობები
	 */
	public void learn(List<Double> x, List<Double> d) {
		// შევინახოთ ძველი წონები
		pushWeights();
		// მოვდოთ სამაგალითო ვექტორი და გამოვავლინოთ ქსელის გადაწყვეტილება
		applyInputs(x);
		resetDeltas();
		desiredOutputs = d;
		for (int l = 0; l < layers.size(); l++) {
			for (int n = 0; n < layers.get(l).size(); n++) {
				for (int i = 0; i <  layers.get(l).get(n).getInputs().size() + 1; i++) {
					modifyNeuronWeight(l, n, i);
				}
				layers.get(l).get(n).incLearnStepCounter();
			}
		}
		learnStepCounter++;
	}
	
	
	/**
	 * ქსელისთვის საწყისი წონების დაყენება
	 */
	public void setWeightsAndBias(List<List<List<Double>>> weights) {
		for (int l = 0; l < layers.size(); l++) {
			for (int n = 0; n < layers.get(l).size(); n++) {
				layers.get(l).get(n).setWeightsAndBias(weights == null ? null : weights.get(l).get(n));
			}
		}
	}
	
	/**
	 * დელტა წესის გამოყენება ნეირონოს წონისთვის
	 * @param layerNumber ნეირონების ფენის ნომერი
	 * @param neuronNumber ნეირონის ნომერი
	 * @param weightNumber ნეირონის წონის ნომერი
	 */
	private void modifyNeuronWeight(int layerNumber, int neuronNumber, int weightNumber) {
		ScalarNeuron neuron = layers.get(layerNumber).get(neuronNumber);
		double momentum = 0;
		double deltaW = learningRate * getDelta(layerNumber, neuronNumber);
		if (weightNumber < neuron.getInputs().size()) {
			deltaW *= neuron.getInputs().get(weightNumber);
			if (oldWeights.get(layerNumber).get(neuronNumber).get(weightNumber) != null) {
				momentum = learningMomentum * (neuron.getWeights().get(weightNumber) - oldWeights.get(layerNumber).get(neuronNumber).get(weightNumber));
			}
			neuron.getWeights().set(weightNumber, neuron.getWeights().get(weightNumber) + momentum + deltaW);
		} else {
			if (oldWeights.get(layerNumber).get(neuronNumber).get(weightNumber) != null) {
				momentum = learningMomentum * (neuron.getB() - oldWeights.get(layerNumber).get(neuronNumber).get(weightNumber));
			}
			neuron.setB(neuron.getB() + momentum + deltaW);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// შემომავალი ვექტორი
		if (inputs != null) {
			sb.append("inputs count: " + inputs.size() + "; ");
			sb.append("( ");
			for (double input : inputs) {
				sb.append(input + " ");
			}
			sb.append(") \n");
		} else {
			sb.append("inputs count: 0 () \n");
		}
		
		// ნეირონული ქსელის სწავლების იტერაციის ნომერი
		sb.append("learnStepCounter: " + learnStepCounter + "\n");
		
		if (layers != null) {
			for (int l = 0; l < layers.size(); l++) {
				if (layers.get(l) != null) {
					sb.append(l + " layer neurons count: " + layers.get(l).size() + "; ");
					sb.append("( \n\n");
					int i = 0;
					for (ScalarNeuron neuron : layers.get(l)) {
						sb.append("neuron: " + i + ";\n");
						sb.append(neuron.toString() + "\n");
						i++;
					}
					sb.append(") \n");
				} else {
					sb.append(l + " Hidden layer neurons count: 0; \n");
				}
			
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * ნეირონის გადაწყვეტილების ვექტორის მიღება
	 */
	public List<Double> getOut() {
		List<Double> outs = new ArrayList<Double>();
		for (ScalarNeuron neuron : layers.get(layers.size() - 1)) {
			outs.add(neuron.getOut());
		}
		return outs;
	}

	/**
	 * შრის გამოსავალი ვექტორის მიღება
	 */
	private List<Double> getLayerOuts(int layerNumber) {
		List<Double> outs = new ArrayList<Double>();
		for (int i = 0; i < layers.get(layerNumber).size(); i++) {
			outs.add(layers.get(layerNumber).get(i).getOut());
		}
		return outs;
	}

	/**
	 * ძველი წონების განულება
	 */
	private void resetOldWeights() {
		processOldWeights(true);		
	}
	
	/**
	 * ძველი წონების განულება
	 */
	private void pushWeights() {
		processOldWeights(false);		
	}

	/**
	 * ძველი წონების შენახვა
	 */
	private void processOldWeights(boolean blank) {
		oldWeights = new ArrayList<List<List<Double>>>();
		for (int i = 0; i < layers.size(); i++) {
			oldWeights.add(new ArrayList<List<Double>>());
			for (ScalarNeuron neuron : layers.get(i)) {
				List<Double> weights = null;
				if (!blank) {
					weights = new ArrayList<Double>(neuron.getWeights());
					weights.add(neuron.getB());
				}
				oldWeights.get(i).add(weights);
			}
		}
	}
	
	/**
	 * ლოკალური გრადიენტების მასივის მომზადება
	 */
	private void resetDeltas() {
		deltas = new ArrayList<List<Double>>();
		for (int l = 0; l < layers.size(); l++) {
			deltas.add(new ArrayList<Double>());
			for (int i = 0; i < layers.get(l).size();  i++) {
				deltas.get(l).add(null);
			}
		}
	}
	
	// L ფენის n-ური ნეირონის ლოკალური გრადიენტის გამოთვლა
	private double getDelta(int l, int n) {
		if (deltas.get(l).get(n) == null) {
			double delta = 0;
			// გამომავალი ნეირონისთვის დელტა პირდაპირ გამოითვლება
			if (l == layers.size() - 1) {
				ScalarNeuron neuron = layers.get(l).get(n);
				delta = (desiredOutputs.get(n) - neuron.getOut()) * neuron.getActivationFunction().derivativeAt(neuron.getNet());
			} else {
				ScalarNeuron neuron = layers.get(l).get(n);
				double sum = 0;
				for (int i = 0; i < layers.get(l + 1).size(); i++) {
					sum += getDelta(l + 1, i) * layers.get(l + 1).get(i).getWeights().get(n);
				}
				delta = neuron.getActivationFunction().derivativeAt(neuron.getNet()) * sum;
			}			
			
			deltas.get(l).set(n, delta);
		}
		
		return deltas.get(l).get(n);
	}
	
	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getLearningMomentum() {
		return learningMomentum;
	}

	public void setLearningMomentum(double learningMomentum) {
		this.learningMomentum = learningMomentum;
	}

	public List<Double> getInputs() {
		return inputs;
	}

	public void setInputs(List<Double> inputs) {
		this.inputs = inputs;
	}

	public List<List<ScalarNeuron>> getLayers() {
		return layers;
	}

}
