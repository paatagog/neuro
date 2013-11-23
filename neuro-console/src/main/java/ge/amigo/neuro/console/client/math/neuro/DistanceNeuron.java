package ge.amigo.neuro.console.client.math.neuro;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ნეირონი, რომელიც ითვლის მანძილს შემომავალ ვექტორსა და საკუთარ წონებს შორის
 */
public class DistanceNeuron implements Neuron {
	
	// შემომავალი ვექტორი
	private List<Double> inputs;
	
	// ნეირონის წონები
	private List<Double> weights;

	// ნეირონის გადაწყვეტილება
	private double out;

	// სწავლების იტერაციის ნომერი
	private int learnStepCounter = 0;
	
	// პირველი სამაგალითო ვექტორი ხდება ათვლის წერტილი სწავლების პროცესში 
	private boolean startLearnWithFirstSample = true;

	/**
	 * შემომავალი ვექტორის საფუძველზე გადაწყვეტილების მიღება
	 * @return მანძილი შემომავალ ვექტორსა და წონებს შორის
	 */
	public double generateOut() {
		out = VectorUtils.norm(VectorUtils.substract(inputs, weights));
		return getOut();
	}
	
	/**
	 * შემომავალი ვექტორის დაყენება და გადაწყვეტილების გამოთვლა
	 */
	public double applyInputs(List<Double> i) {
		setInputs(i);
		return generateOut();
	}

	
	/**
	 * სწავლება სამაგალითო ვექტორის საფუძველზე 
	 * @param sample სამაგალითო ვექტორი
	 */
	public void learn(List<Double> sample) {
		setInputs(sample);
		if (startLearnWithFirstSample && learnStepCounter == 0) {
			setWeights(sample);
		} else {
			// მოვდოთ სამაგალითო ვექტორი და გამოვთვალოთ გადაწყვეტილება
			applyInputs(sample);
			List<Double> distance = VectorUtils.substract(sample, weights);
			setWeights(VectorUtils.add(weights, VectorUtils.prod(learnFactor(learnStepCounter), distance)));
		}
		learnStepCounter ++;
	}
	
	/**
	 * სწავლების ყველა ნაბიჯზე მაკორექტირებელი ვექტორის სიგრძე უნდა შემცირდეს. ეს ფუნქცია ადგენს i-ურ ნაბიჯზე კოეფიციენტს 
	 * @param i ნაბიჯის ნომერი. იცვლება 0-დან უსასრულობამდე
	 * @return მაკორექტირებელი ვექტორის კოეფიციენტი
	 */
	public double learnFactor(int i) {
		double l = 1;
		double a = 1;
		double learnFactor = l / (a * i + 1);
		return learnFactor;
	}

	
	public void setInputs(List<Double> inputs) {
		this.inputs = new ArrayList<Double>(inputs);
	}

	public List<Double> getInputs() {
		return inputs;
	}

	/**
	 * წონების დაყენება
	 */
	public void setWeights(List<Double> weights) {
		this.weights = new ArrayList<Double>(weights);
	}

	public List<Double> getWeights() {
		return weights;
	}
	
	public int getLearnStepCounter() {
		return learnStepCounter;
	}

	public void setLearnStepCounter(int learnStepCounter) {
		this.learnStepCounter = learnStepCounter;
	}

	public void setOut(double out) {
		this.out = out;
	}

	public double getOut() {
		return out;
	}

	public boolean isStartLearnWithFirstSample() {
		return startLearnWithFirstSample;
	}

	public void setStartLearnWithFirstSample(boolean startLearnWithFirstSample) {
		this.startLearnWithFirstSample = startLearnWithFirstSample;
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
			sb.append("inputs count: 0 ()\n");
		}
		
		// წონები
		if (weights != null) {
			sb.append("weights count: " + weights.size() + "; ");
			sb.append("( ");
			for (double input : weights) {
				sb.append(input + " ");
			}
			sb.append(") \n");
		} else {
			sb.append("weights count: 0 ()\n");
		}
		
		// ნეირონის გადაწყვეტილება
		sb.append("out: " + out + "\n");

		// სწავლების იტერაციის ნომერი
		sb.append("learnStepCounter: " + learnStepCounter + "\n");
		
		// პირველი სამაგალითო ვექტორი ხდება ათვლის წერტილი სწავლების პროცესში 
		sb.append("startLearnWithFirstSample: " + startLearnWithFirstSample + "\n");
		
		return sb.toString();
	}
	
	

}
