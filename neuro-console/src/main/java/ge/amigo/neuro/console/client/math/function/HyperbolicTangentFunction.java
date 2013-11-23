package ge.amigo.neuro.console.client.math.function;

public class HyperbolicTangentFunction implements Function {

	/**
	 * პარამეტრი a > 0
	 */
	private double a = 1;

	/**
	 * პარამეტრი b > 0
	 */
	private double b = 1;

	@Override
	public double valueAt(double x) {
		return a * Math.tanh(b * x);
	}

	@Override
	public double derivativeAt(double x) {
		double f = valueAt(x);
		return (b / a) * (a - f) * (a + f);
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

}
