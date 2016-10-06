package cc.kave.episodes.similarityMetrics;

public class TriangleNumber {

	public int calculate(int num) {
		if (num < 2) {
			return 0;
		} else if (num == 2) {
			return 1;
		} else {
			return (num - 1) + calculate(num - 1);
		}
	}
}
