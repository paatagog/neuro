package ge.amigo.neuro.console.client.math.neuro;

import ge.amigo.neuro.console.client.math.calculation.VectorUtils;
import ge.amigo.neuro.console.client.utils.Logger;

import java.util.List;

/**
 * DistandceNetwork კლასი არის ერთშრიანი  ნეირონულ ქსელი.<p>
 * გამოყენების მაგალითი:<p>
 * <code>
 * DistanceNetwork dn = new DistanceNetwork();<br/>
 * List<Double> inputs = new ArrayList<Double>();<br/>
 * dn.setInputs(inputs);<br/>
 * dn.distributeInputs();<br/>
 * dn.generateDistanceLayerOut();<br/>
 * dn.generateOut();<br/>
 * System.out.print(dn.getOut());<br/>
 * </code>
 */
public class DistanceNetwork {
	
	// ნეირონული ქსელის შესასვლელზე არსებული ვექტორი
	private List<Double> inputs;

	// ნეირონების შრე
	private List<DistanceNeuron> distanceLayer;
	
	// ნეირონული ქსელის გადაწყვეტილების მიმღები ნეირონის ნომერი. ეს ის ნეირონია, რომელიც ყველაზე ახლო აღმოჩნდა შესასვლელ ვექტორთან
	private int outNumber;
	
	// ნეირონული ქსელის გადაწყვეტილების მიმღები ნეირონი. ეს ის ნეირონია, რომელიც ყველაზე ახლო აღმოჩნდა შესასვლელ ვექტორთან
	private DistanceNeuron out;
	
	// სწავლების იტერაციის ნომერი
	private int learnStepCounter = 0;
	
	// ნეირონის ხილვადობა. მაქსიმალური მანძილი, რომლზე უფრო მეტად დაშორებული წერტილი ვეღარ იქნება ამოცნობილი ნეირონის მიერ, თუნდაც იგი სხვა ნეირონებზე ახლოს იყოს ამ წერტილთან
	// მანძილი არაა შემოსაზღვრული, თუ ეს პარამეტრი ნულის ტოლია
	private double neuronVisibility = 0;

	// ლოგერის კონფიგურაცია
	private final static Logger log = Logger.getLogger(DistanceNetwork.class.getName()); 
	
	public DistanceNetwork() {
		
	}
	
	/**
	 * ნეირონული ქსელის შესასვლელზე არსებულ ვექტორს წერს თითოეულ ნეირონში
	 */
	public void distributeInputs() {
		if (distanceLayer != null) {
			for (DistanceNeuron neuron : distanceLayer) {
				if (neuron.getLearnStepCounter() != 0) {
					neuron.setInputs(inputs);
				}
			}
		}
	}
	
	/**
	 * ნეირონული ქსელის პირველი შრის ნეირონების მიერ გადაწყვეტილების მიღება
	 */
	public void generateDistanceLayerOut() {
		for (DistanceNeuron neuron : distanceLayer) {
			if (neuron.getLearnStepCounter() > 0) {
				neuron.generateOut();
			}
		}
	}
	
	/**
	 * საწყისი ვექტორის შეშვება ნეირონულ ქსელში და საბოლოო გამოსავალის მიღება.
	 * @param inputs საწყისი ვექტორი
	 * @return უახლოესი ნეირონის ნომერი
	 */
	public int applyInputs(List<Double> inputs) {
		setInputs(inputs);
		distributeInputs();
		generateDistanceLayerOut();
		return generateOut(false);
	}
	
	/**
	 * პირველი შრის ნეირონების მიერ მიღებული გადაწყვეტილების საფუძველზე მინიმალური მანძილის მქონე ნეირონის გამოვლენა
	 * თუ ნერონი ვერ გამოვლინდა, ბრუნდება -1
	 */
	public int generateOut(boolean expandVisibility) {
		double min = 0;
		int minNumber = -1;
		int i = 0;
		for (DistanceNeuron dn : distanceLayer) {
			if (dn.getLearnStepCounter() > 0) {
				if ((minNumber == -1 || min > dn.getOut() ) && dn.getOut() <= neuronVisibility) {
					min = dn.getOut();
					minNumber = i;
				}
			}
			i++;
		}
		outNumber = minNumber;
		out = outNumber == -1 ? null : distanceLayer.get(minNumber);
		return outNumber;
	}
	
	/**
	 * სწავლება სამაგალითო ვექტორის საფუძველზე. თუ სწავლება ვერ მოხდა, learnStepCounter არ შეიცვლება
	 * @param sample სამაგალითო ვექტორი
	 * @param forceNew მოდებული სამაგალითო ვექტორისთვის ახალი ნეირონის გამოყოფა იძულებითი წესით
	 */
	public void learn(List<Double> sample, boolean forceNew) {
		// მოვდოთ სამაგალითო ვექტორი და გამოვავლინოთ უახლოესი ნეირონი
		int closest = applyInputs(sample);
		// თუ მახლობელი ნეირონი მოიძებნა და ამ ნეირონიდან სამაგალითო წერტილამდე მანძილი დასაშვებ ფარგლებშია, მაშინ ამ ნეირონს ვასწავლოთ
		if (closest != -1 && (neuronVisibility == 0 || VectorUtils.norm(VectorUtils.substract(distanceLayer.get(closest).getWeights(), sample)) < neuronVisibility)) {
			distanceLayer.get(closest).learn(sample);
			learnStepCounter ++;
		} else { // გამოვყოთ ცარიელი ნეირონი და ვასწავლოთ
			learnNewNeuron(sample);			
		}
	}

	private int learnNewNeuron(List<Double> sample) {
		int i = 0;
		int neuronNumber = -1;
		for (DistanceNeuron dn : distanceLayer) {
			if (dn.getLearnStepCounter() == 0) {
				dn.setWeights(sample);
				dn.learn(sample);
				neuronNumber = i;
				learnStepCounter ++;
				break;
			}
			i++;
		}
		return neuronNumber;
	}
	
	public void setDistanceLayer(List<DistanceNeuron> distanceLayer) {
		this.distanceLayer = distanceLayer;
	}

	public List<DistanceNeuron> getDistanceLayer() {
		return distanceLayer;
	}

	public void setInputs(List<Double> inputs) {
		this.inputs = inputs;
	}

	public List<Double> getInputs() {
		return inputs;
	}

	public void setOut(DistanceNeuron out) {
		this.out = out;
	}

	public DistanceNeuron getOut() {
		return out;
	}

	public void setOutNumber(int outNumber) {
		this.outNumber = outNumber;
	}

	public int getOutNumber() {
		return outNumber;
	}

	public void setLearnStepCounter(int learnStepCounter) {
		this.learnStepCounter = learnStepCounter;
	}

	public int getLearnStepCounter() {
		return learnStepCounter;
	}

	public static Logger getLog() {
		return log;
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
		
		// მაქსიმალური მანძილი ამოცნობისთვის
		sb.append("neuronVisibility: " + neuronVisibility + "\n");


		if (out != null) {
			// ნეირონული ქსელის გადაწყვეტილება (გამარჯვებული ნეირონი)
			sb.append("out neuron number " + outNumber + ":\n" + out.toString() + "\n");
		}

		// ნეირონების პირველი შრე
		if (distanceLayer != null) {
			// გვჭირდება მხოლოდ ათვისებული ნეირონები
			int count = 0;
			for (DistanceNeuron neuron : distanceLayer) {
				if (neuron.getLearnStepCounter() > 0) {
					count++;
				}
			}
			
			sb.append("first layer active neurons count: " + count + "; ");
			if (count > 0) {
				sb.append("( \n\n");
				int i = 0;
				for (DistanceNeuron neuron : distanceLayer) {
					if (neuron.getLearnStepCounter() > 0) { 
						sb.append("neuron: " + i + ";\n");
						sb.append(neuron.toString() + "\n");
						i++;
					}
				}
				sb.append(") \n");
			}
		} else {
			sb.append("first layer active neurons count: 0; \n");
		}
		
		
		return sb.toString();
	}


	public double getNeuronVisibility() {
		return neuronVisibility;
	}

	public void setNeuronVisibility(double neuronVisibility) {
		this.neuronVisibility = neuronVisibility;
	}

}
