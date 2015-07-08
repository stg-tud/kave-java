package gsonexample.model;

public class Stringbucket implements IBucket<String> {
	char[] characters;

	public Stringbucket() {
	}

	public Stringbucket(String str) {
		setString(str);
	}

	private String getString() {
		return new String(characters);
	}

	private void setString(String string) {
		this.characters = new char[string.length()];
		string.getChars(0, string.length(), this.characters, 0);
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
