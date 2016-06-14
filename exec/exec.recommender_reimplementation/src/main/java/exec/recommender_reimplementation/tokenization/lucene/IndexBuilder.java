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
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cc.kave.commons.model.events.completionevents.Context;
import exec.recommender_reimplementation.ContextReader;

public class IndexBuilder {
	
	public static final String FOLDERPATH = "C:\\SST Datasets\\Testset";

	private IndexWriter indexWriter;

	public Directory indexDir;
	
	public void setIndexWriter() throws IOException {
		indexDir = FSDirectory.open(Paths.get(FOLDERPATH,"index-directory"));
		IndexWriterConfig config = new IndexWriterConfig(new SSTAnalyzer());
		indexWriter = new IndexWriter(indexDir, config);
	}
	
	public void closeIndexWriter() throws IOException {
		if(indexWriter != null) {
			indexWriter.close();
		}
	}
	
	public void indexContextsFromFolder() throws IOException {
		setIndexWriter();
		List<Context> contexts = ContextReader.GetContexts(Paths.get(FOLDERPATH));
		
		for (int i = 0; i < contexts.size(); i++) {
			Context context = contexts.get(i);
			indexContext(context,i);
		}
		
		closeIndexWriter();
	}
	
	public void indexContext(Context context, int index) throws IOException {
		Document doc = new Document();
		SSTTokenizer tokenStream;
		try {
			tokenStream = new SSTTokenizer(context);
		} catch (Exception e) {
			return;
		}
		doc.add(new StringField("class", context.getSST().getEnclosingType().getName(), Field.Store.YES));
		doc.add(new Field("context", tokenStream, getFieldType()));
		indexWriter.addDocument(doc);
	}

	public static FieldType getFieldType() {
		FieldType fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		fieldType.setStoreTermVectors(true);
		fieldType.setStoreTermVectorPositions(true);
		fieldType.setStoreTermVectorOffsets(true);
		fieldType.setTokenized(true);
		return fieldType;
	}
}
