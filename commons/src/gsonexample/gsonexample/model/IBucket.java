package gsonexample.model;

public interface IBucket<T> {

	public T getValue();

	public void setValue(T newvalue);

	public String toString();

}
