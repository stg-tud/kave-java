package gsonexample.model;

import java.util.LinkedList;
import java.util.List;

public class ExtendedBag extends BagOfPrimitives {

	List<IBucket> buckets;

	public void addBucket(IBucket bucket) {
		if (buckets == null)
			buckets = new LinkedList<IBucket>();
		buckets.add(bucket);
	}

	@Override
	public String toString() {
		String str = super.toString();
		for (IBucket bucket : buckets) {
			str = str + (" " + bucket.toString());
		}
		return str;
	}

}
