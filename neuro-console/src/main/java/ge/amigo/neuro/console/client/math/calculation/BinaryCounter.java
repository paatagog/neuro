package ge.amigo.neuro.console.client.math.calculation;

import java.util.ArrayList;
import java.util.List;

public class BinaryCounter extends NaryCounter {
	
	public BinaryCounter(int n) {
		super(2, n);
	}
	
	@Override
	public List<Integer> getNext() {
		List<Integer> result = new ArrayList<Integer>();
		List<Integer> next = super.getNext(); 
		for (int i = 0; i < getN(); i++) {
			result.add(next.get(i) * 2 -1);
		}
		return result;
	}

	@Override
	public void setState(List<Integer> state) {
		List<Integer> transformed = new ArrayList<Integer>();
		for (int i = 0; i < state.size(); i ++) {
			transformed.add(state.get(i) * 2 -1);
		}
		super.setState(transformed);
	}
	
	@Override
	public List<Integer> getState() {
		List<Integer> transformed = new ArrayList<Integer>();
		for (int i = 0; i < super.getState().size(); i ++) {
			transformed.add(super.getState().get(i) * 2 -1);
		}
		return transformed;
	}
 
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Count=" + getN() + "; ");
		sb.append("State=");
		sb.append("(");
		List<Integer> state = getState();
		for (int i = 0; i < getN(); i++) {
			if (state.get(i) == 1) {
				sb.append(" ");
			}
			sb.append(state.get(i));
			if (i != getN() -1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	

}
