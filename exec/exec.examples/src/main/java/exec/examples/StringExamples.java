package exec.examples;

public class StringExamples {

	public static void main(String[] args) {
		
		String str = "Github/OfficeDev/PnP/Samples/Provisioning.Yammer/Provisioning.Yammer.sln-contexts.zip";
		int index = str.indexOf("/", str.indexOf("/", str.indexOf("/") + 1) + 1);
		System.out.println(str + ": " + index);
	}
}
