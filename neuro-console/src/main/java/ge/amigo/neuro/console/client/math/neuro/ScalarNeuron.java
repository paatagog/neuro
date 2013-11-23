package ge.amigo.neuro.console.client.math.neuro;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;
import ge.amigo.neuro.console.client.math.function.Function;
import ge.amigo.neuro.console.client.math.function.SigmoidFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * ნეირონი, რომელიც ითვლის სკალარულ ნამრავლს შემომავალ ვექტორსა და საკუთარ წონებს შორის 
 * და ამის მერე მიღებულ ნამრავლზე იმოქმედებს სიგმოიდური ფუნქციით
 */
public class ScalarNeuron implements Neuron {
	
	// შემომავალი ვექტორი
	private List<Double> inputs;
	
	// ნეირონის წონები
	private List<Double> weights;
	
	// ნეირონის ბაზისი
	private double b;

	// აქტივაციის ფუნქცია
	private Function activationFunction = new SigmoidFunction(1);
	
	// ნეირონის გადაწყვეტილება
	private double out;

	// შემომავალი ვექტორის და წონების სკალარული ნამრავლი
	private double net;

	// სწავლების იტერაციის ნომერი
	private int learnStepCounter = 0;
	
	/**
	 * შემომავალი ვექტორის საფუძველზე გადაწყვეტილების მიღება
	 * @return სკალარული ნამრავლი შემომავალ ვექტორსა და წონებს შორის + აქტივაციის ფუნქცია
	 */
	public double generateOut() {
		net = VectorUtils.dotProd(inputs, weights) + b;
		out = activationFunction.valueAt(net);
		return out;
	}

	/**
	 * შემომავალი ვექტორის დაყენება და გადაწყვეტილების გამოთვლა
	 */
	public double applyInputs(List<Double> i) {
		setInputs(i);
		return generateOut();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// შემომავალი ვექტორი
		if (inputs != null) {
			sb.append("inputs count: " + inputs.size() + "; ");
			sb.append("( ");
			for (Double input : inputs) {
				sb.append(String.valueOf(input) + " ");
			}
			sb.append(") \n");
		} else {
			sb.append("inputs count: 0 ()\n");
		}
		
		// წონები
		if (weights != null) {
			sb.append("weights count: " + weights.size() + "; ");
			sb.append("( ");
			for (Double weight : weights) {
				sb.append(String.valueOf(weight) + " ");
			}
			sb.append(") \n");
		} else {
			sb.append("weights count: 0 ()\n");
		}
		
		// ნეირონის გადაწყვეტილება
		sb.append("basis: " + b + "\n");
		
		// ნეირონის გადაწყვეტილება
		sb.append("out: " + out + "\n");

		// სწავლების იტერაციის ნომერი
		sb.append("learnStepCounter: " + learnStepCounter + "\n");
		
		return sb.toString();
	}
	
	public List<Double> getInputs() {
		return inputs;
	}

	public void setInputs(List<Double> inputs) {
		this.inputs = new ArrayList<Double>(inputs);
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = new ArrayList<Double>(weights);
	}

	public void setWeightsAndBias(List<Double> weights) {
		if (weights == null) {
			weights = new ArrayList<Double>();
			for (int i = 0; i < inputs.size() + 1; i++) {
				weights.add(Math.random() * 2 - 1);
			}
		}
		this.weights = new ArrayList<Double>(weights);
		this.weights.remove(weights.size() - 1);
		this.b = weights.get(weights.size() - 1);
	}

	public double getOut() {
		return out;
	}

	public void setOut(double out) {
		this.out = out;
	}

	public int getLearnStepCounter() {
		return learnStepCounter;
	}

	public void setLearnStepCounter(int learnStepCounter) {
		this.learnStepCounter = learnStepCounter;
	}
	
	public void incLearnStepCounter() {
		learnStepCounter++;
	}

	public Function getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(Function activationFunction) {
		this.activationFunction = activationFunction;
	}

	public double getNet() {
		return net;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}
}
