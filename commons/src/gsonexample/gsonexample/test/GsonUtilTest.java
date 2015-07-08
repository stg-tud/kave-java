package gsonexample.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gsonexample.GsonUtil;
import gsonexample.model.ExtendedBag;
import gsonexample.model.IBucket;
import gsonexample.model.Stringbucket;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class GsonUtilTest {

	@Test
	public void primitiveBagPrintsCorrectly() {
		assertEquals(Fixture.getPrimitiveBag().toString(), "[int: 11 boolean: true string: Hallo]");
	}

	@Test
	public void extendedBagPrintsCorrectly() {
		assertEquals(Fixture.getExtendedBag().toString(),
				"[int: 11 boolean: true string: Hallo] Intbucket: 13 Stringbucket: Welt");
	}

	@Test
	public void createsFileFromExtendedBag() {
		try {
			File file = new File("C:/out.json");
			ExtendedBag extendedBag = Fixture.getExtendedBag();
			GsonUtil.toFileWithType(file, extendedBag, ExtendedBag.class);
			assertTrue(file.exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void createsFileFromSimpleBag() {
		try {
			File file = new File("C:/out.json");
			Fixture.getPrimitiveBag().serialize(file);
			assertTrue(file.exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void deserializesFromFile() {
		try {
			ExtendedBag testBag = GsonUtil.fromFile(new File("C:/out.json"), ExtendedBag.class);
			assertEquals(testBag, Fixture.getExtendedBag());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void createsFileFromIBucket() {
		try {
			File file = new File("C:/bucket.json");
			IBucket bucket = Fixture.getStringbucket("Stringvalue");
			GsonUtil.toFile(file, bucket);
			assertTrue(file.exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void serializesWithCustomDeserializer() {
		ExtendedBag extendedBag = Fixture.getExtendedBag();
		System.out.println(GsonUtil.toStringSerializertest(extendedBag));

	}

	@Test
	public void deserializesBucketFromFile() {
		try {
			IBucket bucket = GsonUtil.fromFile(new File("C:/bucket.json"), IBucket.class);
			assertEquals(bucket.toString(), new Stringbucket("Stringvalue").toString());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
