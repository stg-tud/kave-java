package example.classes;

import java.util.ArrayList;
import java.util.List;

public class TestClass extends TestSuperClass{

	public int intTest = 42;
	public String stringTest = "Test";
	public static final int STATIC_TEST = 7;
	private int accessModifier = 5;

	public TestClass() {

	}

	public TestClass(String a, int b) {

	}

	public int method() {
		return 2;
	}

	public int method(int a) {
		return 2 + a;
	}

	public String returnsObject() {
		return "Test";
	}

	public static void staticMethod() {
	}

	public void parametersMethod(int a, String b, String... c) {
	}

	public void statementMethod() {
		int[] array = { 1, 2, 3, 4, 5 };
		
		for (int i = 0; i < array.length; i++) {
			int loopInteger = i;
		}

		int localVariable = 1;
		String localObject = "Test";
		List<String> interfaceType = new ArrayList<String>();
		TestEnum enumType = TestEnum.FRIDAY;

		this.accessMethod();
		super.equals(this);
	}

	private void accessMethod() {
		
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
