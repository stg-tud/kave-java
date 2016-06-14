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
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cc.kave.commons.model.events.completionevents.Context;
import exec.recommender_reimplementation.tokenization.TokenExtractor;

public class SSTTokenizer extends Tokenizer{
	
	private List<String> tokenStream;
	
	protected int position = 0;
	
	public SSTTokenizer(Context context) {
		this.tokenStream = TokenExtractor.extractTokenStream(context);
	}

	protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
	
	@Override
	public boolean incrementToken() throws IOException {
		charTermAttribute.setEmpty();
		
		if(position + 1 < tokenStream.size()) {
			String nextToken = tokenStream.get(position);
			charTermAttribute.append(nextToken);
			position++;
			return true;
		}
		return false;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		position = 0;
	}

}
