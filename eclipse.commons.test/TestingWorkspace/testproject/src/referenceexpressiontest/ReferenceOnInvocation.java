package referenceexpressiontest;

public class ReferenceOnInvocation {

	ReferenceOnInvocation b;

	public void method(ReferenceOnInvocation a) {
		ReferenceOnInvocation c = a.getB().b;
	}

	public ReferenceOnInvocation getB() {
		return b;
	}
}
