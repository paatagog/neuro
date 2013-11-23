package ge.amigo.neuro.console.client.math.neuro;

import ge.amigo.neuro.console.client.math.calculation.BinaryCounter;
import ge.amigo.neuro.console.client.math.calculation.VectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ორობითი ნეირონი, რომელიც ითვლის სკალარულ ნამრავლს შემომავალ ვექტორსა და საკუთარ წონებს შორის, აკლებს ზღურბლს და იღებს ნიშანს
 * შემომავალი სიგნალებია +1 ან -1. გადაწყვეტილებაც არის +1 ან -1
 */
public class BinaryNeuron implements Neuron {
	
	// შემომავალი ვექტორი
	private List<Integer> inputs;
	
	// ნეირონის წონები
	private List<Double> weights;

	// ნეირონის არხების შცდომის ალბათობები
	private List<Double> channelErrors;

	// ნეირონის გადაწყვეტილება
	private int out;

	// სწავლების იტერაციის ნომერი
	private int learnStepCounter = 0;

	/**
	 * შემომავალი ვექტორის საფუძველზე გადაწყვეტილების მიღება
	 * @return ნეირონის გადაწყვეტილება, +1 ან -1
	 */
	public int generateOut() {
		out = (int) Math.signum(VectorUtils.dotProd(inputs, weights));
		return out;
	}
	
	/**
	 * შემომავალი ვექტორის დაყენება და გადაწყვეტილების მიღება
	 */
	public double applyInputs(List<Integer> i) {
		inputs = i;
		return generateOut();
	}

	
	/**
	 * სწავლება სამაგალითო ვექტორის საფუძველზე 
	 * @param sample სამაგალითო ვექტორი
	 */
	public void learn(List<Integer> sample, Integer out) {
		applyInputs(sample);
		// TODO
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
	
	public double getProbabilityOfError() {
		return getProbabilityOfError(this.weights, this.channelErrors);
	}
	
	public double getEnthropicProbabilityOfError() {
		return getProbabilityOfError(getEnthropicWeights(), this.channelErrors);
	}
	
	private List<Double> getEnthropicWeights() {
		List<Double> result = new ArrayList<Double>();
		for (Double q : channelErrors) {
			result.add(Math.log((1-q)/q));
		}
		return result;
	}

	public double getMahalanobisProbabilityOfError() {
		return getProbabilityOfError(getMahalanobisWeights(), this.channelErrors);
	}
	
	private List<Double> getMahalanobisWeights() {
		List<Double> result = new ArrayList<Double>();
		for (Double q : channelErrors) {
			result.add((1-2*q)/(2*q*(1-q)));
		}
		return result;
	}

	public double getProbabilityOfError(List<Double> weights, List<Double> channelErrors) {
		double q = 0;
		BinaryCounter bc = new BinaryCounter(weights.size());
		q = getQ(bc.getState(), weights, channelErrors);
		while (bc.hasNext()) {
			q += getQ(bc.getNext(), weights, channelErrors);
		}		
		return q;
	}

	private double getQ(List<Integer> signs, List<Double> weights, List<Double> channelErrors) {
		int sum = 0;
		double prod = 1;
		for (int i = 0; i < weights.size(); i++) {
			sum += signs.get(i) * weights.get(i);
			prod *= signs.get(i) == -1 ? channelErrors.get(i) : (1 - channelErrors.get(i));
		}
		if (sum >= 0) {
			return prod = 0;
		}
		return prod;
	}
	
	public double getEquivalencyRadius() {
		BinaryCounter bc = new BinaryCounter(weights.size());
		double r = getR(bc.getState());
		while (bc.hasNext()) {
			double q = getR(bc.getNext());
			if (r > q) {
				r = q;
			}
		}		
		return r;
	}

	private double getR(List<Integer> signs) {
		double sum = 0;
		for (int i = 0; i < weights.size(); i++) {
			sum += signs.get(i) * weights.get(i);
		}
		return Math.abs(sum);
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
		
		// არხების შეცდომის ალბათობები
		if (channelErrors != null) {
			sb.append("channel errors count: " + channelErrors.size() + "; ");
			sb.append("( ");
			for (double error : channelErrors) {
				sb.append(error + " ");
			}
			sb.append(") \n");
		} else {
			sb.append("channel errors count: 0 ()\n");
		}

		// ნეირონის გადაწყვეტილება
		sb.append("out: " + out + "\n");

		// სწავლების იტერაციის ნომერი
		sb.append("learnStepCounter: " + learnStepCounter + "\n");
		
		return sb.toString();
	}		
	
	public List<Integer> getInputs() {
		return inputs;
	}

	public void setInputs(List<Integer> inputs) {
		this.inputs = inputs;
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}

	public int getLearnStepCounter() {
		return learnStepCounter;
	}

	public void setLearnStepCounter(int learnStepCounter) {
		this.learnStepCounter = learnStepCounter;
	}

	public List<Double> getChannelErrors() {
		return channelErrors;
	}

	public void setChannelErrors(List<Double> channelErrors) {
		this.channelErrors = channelErrors;
	}
	

	

}
