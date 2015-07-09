package gsonexample.test;

import static org.junit.Assert.assertEquals;
import gsonexample.GsonUtil;
import gsonexample.model.BagOfPrimitives;
import gsonexample.model.ExtendedBag;

import org.junit.Test;

public class TypeDiscriminatorTest {

	@Test
	public void writesDiscriminator() {
		BagOfPrimitives input = Fixture.getPrimitiveBag();
		String json = GsonUtil.toString(input);
		assertEquals(
				"{\"$type\":\"gsonexample.model.BagOfPrimitives\",\"integer\":11,\"bulean\":true,\"string\":\"Hallo\"}",
				json);
	}

	@Test
	public void writesDiscriminators() {
		ExtendedBag input = Fixture.getExtendedBag();
		String json = GsonUtil.toString(input);
		assertEquals(
				"{\"$type\":\"gsonexample.model.ExtendedBag\",\"buckets\":[{\"$type\":\"gsonexample.model.Intbucket\",\"Int\":13},{\"$type\":\"gsonexample.model.Stringbucket\",\"characters\":\"Welt\"}],\"integer\":11,\"bulean\":true,\"string\":\"Hallo\"}",
				json);
	}
}
