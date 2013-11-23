package ge.amigo.neuro.console.client.math.neuro.test;

import ge.amigo.neuro.console.client.math.neuro.BinaryNeuron;

import java.util.ArrayList;
import java.util.List;



public class Test1 {
	public static void main(String[] args) {
		BinaryNeuron bn = new BinaryNeuron();
		List<Double> weights = new ArrayList<Double>();
		weights.add(1d);
		weights.add(1d);
		weights.add(1d);
		weights.add(0d);
		bn.setWeights(weights);
		List<Double> q = new ArrayList<Double>();
		q.add(0.12);
		q.add(0.12);
		q.add(0.12);
		q.add(0.5);
		bn.setChannelErrors(q);
		System.out.println("შეცდომის ალბათობაა: " + bn.getProbabilityOfError());
		System.out.println("ენტროპიული წონებით შეცდომის ალბათობაა: " + bn.getEnthropicProbabilityOfError());
		System.out.println("მაჰალანობისის წონებით შეცდომის ალბათობაა: " + bn.getMahalanobisProbabilityOfError());
	}
}
