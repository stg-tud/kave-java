package gsonexample.instancecreator;

import gsonexample.model.IBucket;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class IBucketInstanceCreator implements InstanceCreator<IBucket> {

	@Override
	public IBucket createInstance(Type type) {
		return null;
	}

}
