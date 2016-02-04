/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package smile.learning;

import smile.Network;
import smile.Wrapper;

public class EM extends Wrapper
{
	public native void learn(DataSet data, Network net, DataMatch[] matching, int[] fixedNodes);
	public native void learn(DataSet data, Network net, DataMatch[] matching, String[] fixedNodes);
	
	public void learn(DataSet data, Network net, DataMatch[] matching) { learn(data, net, matching, (int [])null); }
		
	public native void setEqSampleSize(int size);
	public native int getEqSampleSize();
	
	public native void setRandomizeParameters(boolean value);
	public native boolean getRandomizeParameters();

	public native void setRelevance(boolean value);
	public native boolean getRelevance();

	protected native long createNative(Object param);
	protected native void deleteNative(long nativePtr);
}
