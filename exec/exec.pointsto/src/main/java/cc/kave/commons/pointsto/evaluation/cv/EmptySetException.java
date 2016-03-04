package cc.kave.commons.pointsto.evaluation.cv;

public class EmptySetException extends RuntimeException {

	private static final long serialVersionUID = -5080699375994105161L;

	public EmptySetException() {
		super("Set must not be empty");
	}
}
