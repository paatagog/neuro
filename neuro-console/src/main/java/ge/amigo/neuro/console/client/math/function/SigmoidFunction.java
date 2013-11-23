package ge.amigo.neuro.console.client.math.function;

/**
 * სიგმოიდური ფუნქცია
 */
public class SigmoidFunction implements Function {

	/**
	 * პარამეტრი a > 0
	 */
	private double a = 1;

	public SigmoidFunction() {
		
	}
	
	public SigmoidFunction(double parameter) {
		this.a = parameter;
	}
	
	@Override
	public double valueAt(double x) {
		return 1 / (1 + Math.exp(-a * x));
	}

	@Override
	public double derivativeAt(double x) {
		double fi = valueAt(x);
		return a * fi * (1-fi);
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

}
