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

import smile.Wrapper;
import smile.Network;

public class DataSet extends Wrapper 
{
	public final static int DefaultMissingInt = -1;
	public final static float DefaultMissingFloat = (float) Math.sqrt(-1.0);
	
    public native void readFile(String filename, String missingValueToken, int missingInt, float missingFloat, boolean columnIdsPresent);
    public void readFile(String filename, String missingValueToken) 
    { 
        readFile(filename, missingValueToken, DefaultMissingInt, DefaultMissingFloat, true);
    }
    public void readFile(String filename) { readFile(filename, null); }

	public native DataMatch[] matchNetwork(Network net);

	public native int getRecordCount();
	public native int getVariableCount();
	
	public native void addEmptyRecord();

	public native String getVariableId(int variable);
	
	public native int getInt(int variable, int record);
	public native void setInt(int variable, int record, int value);
	
	public native float getFloat(int variable, int record);
	public native void setFloat(int variable, int record, float value);
	
	public native void addIntVariable(String id, int missingValue);
	public native void addFloatVariable(String id, float missingValue);
	public void addFloatVariable(String id) { addFloatVariable(id, DefaultMissingFloat); }
	public void addIntVariable(String id) { addIntVariable(id, DefaultMissingInt); }
	
	public native String[] getStateNames(int variable);
	public native void setStateNames(int variable, String[] names);
	
    protected native long createNative(Object param);
	protected native void deleteNative(long nativePtr);
}
