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
package smile;

import cc.recommenders.nativelibs.NativeLibLoader;

public abstract class Wrapper {
	public Wrapper() {
		ptrNative = createNative(null);
	}

	public Wrapper(Object param) {
		ptrNative = createNative(param);
	}

	protected void finalize() {
		deleteNative(ptrNative);
	}

	protected abstract long createNative(Object param);

	protected abstract void deleteNative(long nativePtr);

	private static native void nativeStaticInit();

	protected long ptrNative = 0;

	static {
		// was: System.loadLibrary("jsmile");
		new NativeLibLoader().loadLibrary("jsmile");

		nativeStaticInit();
	}
}
