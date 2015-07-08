package gsonexample.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BagOfPrimitives {
	int integer;
	boolean bulean;
	String string;

	public BagOfPrimitives() {
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

	public boolean isBulean() {
		return bulean;
	}

	public void setBulean(boolean bulean) {
		this.bulean = bulean;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return ("[" + "int: " + integer + " boolean: " + bulean + " string: " + string + "]");
	}

	public String serialize() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

	public void serialize(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(this.serialize());
		writer.flush();
		writer.close();
	}

}
