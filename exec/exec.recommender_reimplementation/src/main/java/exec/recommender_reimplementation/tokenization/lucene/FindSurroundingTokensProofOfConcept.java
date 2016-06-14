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
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.SpanWeight.Postings;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.BytesRef;

// based on https://lucidworks.com/blog/2013/05/09/update-accessing-words-around-a-positional-match-in-lucene-4/
public class FindSurroundingTokensProofOfConcept {

	public static void main(String[] args) throws IOException {
		IndexBuilder indexBuilder = new IndexBuilder();
		
		indexBuilder.indexContextsFromFolder();
		
		// Example search
		
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(indexBuilder.indexDir));
		
		SpanTermQuery stringQ = new SpanTermQuery(new Term("context", "String"));
		
		IndexReader reader = searcher.getIndexReader();
		LeafReader pseudoAtomicReader = SlowCompositeReaderWrapper.wrap(reader);
		SpanWeight spanWeight = stringQ.createWeight(searcher, false);
		Spans spans = spanWeight.getSpans(pseudoAtomicReader.getContext(), Postings.POSITIONS);

		printSpans(reader, spans);
	}

	public static void printSpans(IndexReader reader, Spans spans) throws IOException {
		int window = 2;	//get the words within two of the match
		
		int nextDoc = 0;
		while ((nextDoc = spans.nextDoc()) != Spans.NO_MORE_DOCS) {
			int docId = nextDoc;
			Map<Integer, String> entries = new HashMap<Integer, String>();
			spans.nextStartPosition();
			System.out.println("Doc: " + docId + " Start: " + spans.startPosition() + " End: " + spans.endPosition());
			int start = spans.startPosition() - window;
			int end = spans.endPosition() + window;
			Terms content = reader.getTermVector(docId, "context");
			TermsEnum termsEnum = content.iterator();
			BytesRef term;
			while ((term = termsEnum.next()) != null) {
				String s = new String(term.bytes, term.offset, term.length);
				PostingsEnum positionsEnum = termsEnum.postings(null, PostingsEnum.ALL);
				if (positionsEnum.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
					int i = 0;
					int position = -1;
					while (i < positionsEnum.freq() && (position = positionsEnum.nextPosition()) != -1) {
						if (position >= start && position <= end) {
							entries.put(position, s);
						}
						i++;
					}
				}
			}
			System.out.println("Entries:" + entries);
		}
	}

}
