/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.tokenization.lucene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.FreeTextSuggester;
import org.apache.lucene.util.BytesRef;

public class MinimumSuggesterExample {
	
	public static void main(String[] args) throws IOException {
		FreeTextSuggester suggester = new FreeTextSuggester(new StandardAnalyzer());
		suggester.build(new TextFileIterator(Paths.get("C:\\luceneTesting\\")));
		List<LookupResult> results;
        results = suggester.lookup("Tech", false, 10);
        for (LookupResult lookupResult : results) {
			System.out.println(lookupResult.toString());
		}
	}

}

class TextFileIterator implements InputIterator {

	private Iterator<Path> iterator;

	private Path currentPath;
	
	public TextFileIterator(Path folderPath) throws IOException {
		List<Path> filesList = Files.walk(folderPath).filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".txt")).collect(Collectors.toList());
		iterator = filesList.iterator();
	}
	
	@Override
	public BytesRef next() throws IOException {
	    if(iterator.hasNext()) {
	    	currentPath = iterator.next();
	    	return new BytesRef(Files.readAllBytes(currentPath));
	    }
	    return null;
	}

	@Override
	public long weight() {
		return 0;
	}

	@Override
	public BytesRef payload() {
		return null;
	}

	@Override
	public boolean hasPayloads() {
		return false;
	}

	@Override
	public Set<BytesRef> contexts() {
		return null;
	}

	@Override
	public boolean hasContexts() {
		return false;
	}

}
