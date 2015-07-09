package gsonexample.model;

public class Stringbucket implements IBucket<String> {
	String characters;

	public Stringbucket() {
	}

	public Stringbucket(String str) {
		setString(str);
	}

	private String getString() {
		return new String(characters);
	}

	private void setString(String string) {
		this.characters = string;
	}

	@Override
	public String toString() {
		return "Stringbucket: " + getString();
	}

	@Override
	public String getValue() {
		return getString();
	}

	@Override
	public void setValue(String newvalue) {
		setString(newvalue);
	}

}
