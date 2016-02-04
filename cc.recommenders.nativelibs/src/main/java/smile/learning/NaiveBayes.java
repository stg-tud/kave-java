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

public class NaiveBayes extends Wrapper
{
    public class PriorsType 
	{
		public final static int K2 = 0;
		public final static int BDeu = 1;
	}

    public native Network learn(DataSet data);

	public native void setClassVariableId(String id);
	public native String getClassVariableId();
	
	public native void setFeatureSelection(boolean value);
	public native boolean getFeatureSelection();
	
	public native void setNetWeight(double weight);
	public native double getNetWeight();
	
	public native void setPriorsMethod(int method);
	public native int getPriorsMethod();

	protected native long createNative(Object param);
	protected native void deleteNative(long nativePtr);
}
