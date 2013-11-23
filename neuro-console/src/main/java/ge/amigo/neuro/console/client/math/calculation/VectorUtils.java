package ge.amigo.neuro.console.client.math.calculation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class VectorUtils {

	public static List<Double> getVector(int size, Double value) {
		List<Double> vector = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			vector.add(value);
		}
		return vector;
	}
	
	public static List<Double> add(List<Double> a, List<Double> b) {
		List<Double> res = new ArrayList<Double>();
		for (int i = 0; i < a.size(); i++) {
			res.add(a.get(i) + b.get(i));
		}
		return res;
	}

	public static List<Double> substract(List<Double> a, List<Double> b) {
		List<Double> res = new ArrayList<Double>();
		for (int i = 0; i < a.size(); i++) {
			res.add(a.get(i) - b.get(i));
		}
		return res;
	}

	public static List<Double> prod(Double a, List<Double> v) {
		List<Double> res = new ArrayList<Double>();
		for (int i = 0; i < v.size(); i++) {
			res.add(a * v.get(i));
		}
		return res;
	}

	public static Double dotProd(List a, List b) {
		Double sum = 0d;
		for (int i = 0; i < a.size(); i++) {
			sum += (Double)a.get(i) * (Double)b.get(i);
		}
		return sum;
	}

	public static Double norm2(List<Double> a) {
		return dotProd(a, a);
	}

	public static Double norm(List<Double> a) {
		return Math.sqrt(norm2(a));
	}
	
	public static Double distance2(List<Double> a, List<Double> b) {
		return norm(substract(a, b));
	}
	
	public static Double distance(List<Double> a, List<Double> b) {
		return Math.sqrt(distance(a, b));
	}
	
	public static List<Double> mean(int n, List<Double> v) {
		List<Double> m = new ArrayList <Double>();
		for (int i = 0; i < m.size(); i++) {
			m.add(0d);
		}
		for (int i = 0; i < v.size(); i++) {
			int j = i % n;
			m.set(j, m.get(j) + v.get(i));
		}
		for (int i = 0; i < m.size(); i++) {
			m.set(i, m.get(i) / n);
		}
		return m;
	}
	
	public static List<Double> getVector(String vectorString, String separator) {
		List<Double> vector = new ArrayList<Double>();
		String[] v = new String[0];
		if (vectorString != null) {
			v = vectorString.split(separator);
		}
		try {
			for (String s : v) {
				vector.add(Double.parseDouble(s.trim()));
			}
		} catch (NumberFormatException ignore) {
		}
		return vector;
	}
	
	public static String toString(List<Double> vector) {
		StringBuilder sb = new StringBuilder();
		if (vector != null) {
			for (Double d : vector) {
				if (d != null) {
					sb.append(d + ", ");
				}
			}
		}
		String res = sb.toString();
		return res.length() == 0 ? res : res.substring(0, res.length() - 2);
	}

}
