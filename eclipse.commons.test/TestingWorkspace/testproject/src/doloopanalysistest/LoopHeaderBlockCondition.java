package doloopanalysistest;

public class LoopHeaderBlockCondition {

	public void method() {
		do {
		} while (isX());
	}

	public boolean isX() {
		return true;
	}
}
