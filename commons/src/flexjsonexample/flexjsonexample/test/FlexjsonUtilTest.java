package flexjsonexample.test;

import org.junit.Test;

import flexjsonexample.FlexjsonUtil;
import gsonexample.test.Fixture;

public class FlexjsonUtilTest {

	@Test
	public void writesToFile() {

	}

	@Test
	public void writesToConsole() {
		System.out.println(FlexjsonUtil.toString(Fixture.getExtendedBag()));
	}

}
