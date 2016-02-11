package instanceofexpressionanalysistest;

public class InvocationInstanceOf {

	public void method() {
		Object o = "";
		
		if (getObject() instanceof Object) {
		}
	}
	
	public Object getObject(){
		return new Object();
	}
}
