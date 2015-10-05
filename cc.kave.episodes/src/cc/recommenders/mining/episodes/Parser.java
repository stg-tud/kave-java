/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package cc.recommenders.mining.episodes;

import static cc.recommenders.assertions.Asserts.assertFalse;
import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Parser {
	
	public List<String> parse(File file) {
		assertTrue(file.exists(), "Frequent episode file does not exist");
		assertFalse(file.isDirectory(), "Frequent episode file is not a file, but a directory");
		
		List<String> lines = new LinkedList<String>();
		
		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return lines;
	}
}
