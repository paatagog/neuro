package ge.amigo.neuro.console.client.math.calculation;

import java.util.ArrayList;
import java.util.List;

/**
 * ითვლის n-ობით სისტემაში 
 */
public class NaryCounter {
	
	/**
	 *  რამდენი თანრიგის დათბვლა გვინდა
	 */
	private int n;
	
	/**
	 *  თვლის სისტემის ფუძე
	 */
	private int base;

	/**
	 * თვლის მიმდინარე მდგომარეობა
	 */
	private List<Integer> state;
	
	/**
	 * კონსტრუქტორი
	 */
	public NaryCounter() {
		
	}

	/**
	 * კონსტრუქტორი
	 * @param base თვლის სისტემა
	 * @param n თანრიგების რაოდენობა
	 */
	public NaryCounter(int base, int n) {
		this.base = base;
		this.n = n;
		reset();
	}
	
	/**
	 * პროცესის თავიდან დაწყება
	 */
	public void reset() {
		state = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			state.add(0);
		}
	}
	
	/**
	 * შესაძლებელია თუ არა კიდევ ერთის დამატება
	 * @return აბრუნებს ჭეშმარიტ მნიშვნელობას თუ შესაძლებელია ერთის დამატება, მცდარს - თუ შეუძლებელია
	 */
	public boolean hasNext() {
		for (int i = 0; i < n; i++) {
			if (state.get(i) != base -1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ერთის დამატება არსებული მდგომარეობისთვის
	 */
	public List<Integer> getNext() {
		inc(0);
		return state;		
	}
	
	private boolean inc(int i) {
		if (state.get(i) < base - 1) {
			state.set(i, state.get(i) + 1);
		} else {
			state.set(i, 0);
			if (i != n -1) {
				inc(i+1);
			} else {
				return false;
			}
		}
		return true;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public List<Integer> getState() {
		return state;
	}

	public void setState(List<Integer> state) {
		this.state = state;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Base=" + base + "; ");
		sb.append("Count=" + n + "; ");
		sb.append("State=");
		sb.append("(");
		for (int i = 0; i < n; i++) {
			sb.append(state.get(i));
			if (i != n -1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
