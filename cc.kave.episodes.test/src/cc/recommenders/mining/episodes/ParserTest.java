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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.recommenders.exceptions.AssertionException;

public class ParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	private Parser sut;
	
	@Before
	public void setup() {
		sut = new Parser();
	}
	
	@Test(expected = AssertionException.class)
	public void nonExistingOutFileThrowException() {
		sut.parse(new File("does not exist"));
	}
	
	@Test(expected = AssertionException.class)
	public void nonExistingParentOutFileThrowException() {
		sut.parse(new File("folder/does not exist"));
	}

	@Test(expected = AssertionException.class)
	public void youCannotPassFoldersToParse() {
		sut.parse(rootFolder.getRoot());
	}
	
	@Test
	public void parserTest() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1, {return type 1, method definition 1, method name 1}\n");
		sb.append("2, {return type 2, method definition 2, method name 2}\n");
		sb.append("3, {return type 3, method definition 3, method name 3}\n");
		sb.append("4, {return type 4, method definition 4, method name 4}\n");
		sb.append("5, {return type 5, method definition 5, method name 5}");
				
		String content = sb.toString();
		
		File file = getFilePath();
		
		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<String> expecteds = new LinkedList<String>();
		
		expecteds.add("1, {return type 1, method definition 1, method name 1}");
		expecteds.add("2, {return type 2, method definition 2, method name 2}");
		expecteds.add("3, {return type 3, method definition 3, method name 3}");
		expecteds.add("4, {return type 4, method definition 4, method name 4}");
		expecteds.add("5, {return type 5, method definition 5, method name 5}");
		
		List<String> actual = sut.parse(file);
		 
		 assertEquals(expecteds, actual);
		 
		 file.delete();
	}	
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventMapping.txt");
		return fileName;
	}
}
