package referenceexpressiontest;

public class ReferenceOnInvocation {

	ReferenceOnInvocation a;
	ReferenceOnInvocation b;

	public void method() {
		ReferenceOnInvocation c = a.getB().b;
	}

	public ReferenceOnInvocation getB() {
		return b;
	}
}
