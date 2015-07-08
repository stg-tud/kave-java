package gsonexample.test;

import gsonexample.model.BagOfPrimitives;
import gsonexample.model.ExtendedBag;
import gsonexample.model.IBucket;
import gsonexample.model.Intbucket;
import gsonexample.model.Stringbucket;

public class Fixture {
	public static BagOfPrimitives getPrimitiveBag() {
		BagOfPrimitives bag = new BagOfPrimitives();
		bag.setBulean(true);
		bag.setInteger(11);
		bag.setString("Hallo");
		return bag;
	}

	public static ExtendedBag getExtendedBag() {
		ExtendedBag bag = new ExtendedBag();
		bag.setBulean(true);
		bag.setInteger(11);
		bag.setString("Hallo");
		bag.addBucket(getIntbucket(13));
		bag.addBucket(getStringbucket("Welt"));
		return bag;
	}

	public static IBucket getIntbucket(int i) {
		return new Intbucket(i);
	}

	public static IBucket getStringbucket(String str) {
		return new Stringbucket(str);
	}

}
