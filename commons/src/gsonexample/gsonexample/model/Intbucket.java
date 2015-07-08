package gsonexample.model;

public class Intbucket implements IBucket<Integer> {
	private int Int;

	public Intbucket(int i) {
		Int = i;
	}

	public Intbucket() {
	}

	@Override
	public String toString() {
		return "Intbucket: " + Int;
	}

	@Override
	public Integer getValue() {
		return Int;
	}

	@Override
	public void setValue(Integer newvalue) {
		this.Int = newvalue;
	}
}
