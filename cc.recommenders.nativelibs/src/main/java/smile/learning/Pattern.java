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

public class Pattern extends Wrapper
{
	public class EdgeType
	{
		public static final int None = 0;
		public static final int Undirected = 1;
		public static final int Directed = 2;
	}

	public native int getSize();
    public native void setSize(int size);
    public native int getEdge(int from, int to);
    public native void setEdge(int from, int to, int type);
    public native boolean hasCycle();
    public native boolean isDAG();
    public native Network makeNetwork(DataSet ds);
	
	protected native long createNative(Object param);
	protected native void deleteNative(long nativePtr);
}

