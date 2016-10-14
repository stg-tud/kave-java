package exec.episode.example;

public class StringBuilderSplitter {

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("a,1\n");
		sb.append("b,2\n");
		sb.append("c,3\n");
		
		String[] stringArray = sb.toString().split("\n");
		
		System.out.println("Test the logger");
		
		for (String string : stringArray) {
			System.out.println(string);
		}
	}
}
