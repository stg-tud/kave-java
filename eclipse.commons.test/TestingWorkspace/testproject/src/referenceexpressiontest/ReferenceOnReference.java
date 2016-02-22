package referenceexpressiontest;

public class ReferenceOnReference {

	ReferenceOnReference a;
	ReferenceOnReference b;

	public void method(ReferenceOnReference a) {
		ReferenceOnReference c = a.b.a;
	}

}
