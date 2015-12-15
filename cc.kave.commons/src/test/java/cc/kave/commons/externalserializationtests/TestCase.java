/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TestCase {
	@Nonnull
	public final String name;

	@Nonnull
	public final Class<?> serializedType;

	@Nonnull
	public final String input;

	@Nonnull
	public final String expectedCompact;

	@Nullable
	public final String expectedFormatted;

	public TestCase(String name, Class<?> serializedType, String input, String expectedCompact,
			String expectedFormatted) {
		this.name = name;
		this.serializedType = serializedType;
		this.input = input;
		this.expectedCompact = expectedCompact;
		this.expectedFormatted = expectedFormatted;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
