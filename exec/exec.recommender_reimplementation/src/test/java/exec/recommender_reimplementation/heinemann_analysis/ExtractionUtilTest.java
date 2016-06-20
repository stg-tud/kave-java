package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;

import com.google.common.collect.Lists;

import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class ExtractionUtilTest extends PBNAnalysisBaseTest {

	@Test
	public void camelCaseSplitJavaTokens() {
		List<String> identifiers = Lists.newArrayList("IOException", "String", "errorMessage", "e", "getMessage");
		List<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertThat(splitIdentifiers, Matchers.contains("io", "exception", "string", "error", "message", "e", "get", "message"));
	}
	
	@Test
	public void camelCaseSplitCSharpTokens() {
		List<String> identifiers = Lists.newArrayList("IOException", "String", "errorMessage", "e", "GetMessage");
		List<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertThat(splitIdentifiers, Matchers.contains("io", "exception", "string", "error", "message", "e", "get", "message"));
	}
	
	@Test
	public void emptyListCamelCaseSplit() {
		List<String> identifiers = new ArrayList<>();
		List<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertTrue(splitIdentifiers.isEmpty());
	}
	
	@Test
	public void stemsTokens() {
		List<String> identifiers = Lists.newArrayList("exception", "get", "message");
		List<String> stemmedIdentifiers = ExtractionUtil.stemTokens(identifiers);
		assertThat(stemmedIdentifiers, Matchers.contains("except", "get", "messag"));
	}
	
	@Test
	public void noDuplicatesAfterStemming() {
		List<String> identifiers = Lists.newArrayList("message", "e", "get", "message");
		List<String> stemmedIdentifiers = ExtractionUtil.stemTokens(identifiers);
		assertThat(stemmedIdentifiers, Matchers.contains("messag","e", "get"));
	}
	
	@Test
	public void collectTokenTest() {
		List<String> tokens = Lists.newArrayList("readFile", "<catch>", "IOException", "e", "String", "errorMessage", "e", "getMessage");
		List<String> identifiers = ExtractionUtil.collectTokens(tokens, 5, false);
		assertThat(identifiers, Matchers.contains("String", "errorMessage", "e", "getMessage"));
	}
	
	@Test
	public void filterSingleCharactersTest() {
		List<String> identifiers = Lists.newArrayList("IOException", "String", "errorMessage", "e", "getMessage");
		ExtractionUtil.filterSingleCharacterIdentifier(identifiers);
		assertThat(identifiers, Matchers.contains("IOException", "String", "errorMessage", "getMessage"));
	}
	
	@Test
	public void isStopWordTest() {
		assertTrue(ExtractionUtil.isStopWord("a"));
	}
	
	@Test
	public void createsNewEntry() {
		List<String> identifiers = Lists.newArrayList("io", "except", "string", "error", "message", "get");
		IMethodName methodName = method(voidType, type("JOptionPane"), "showMessageDialog", parameter(type("Component"), "component"), parameter(objectType, "message"));
		Entry entry = ExtractionUtil.createNewEntry(methodName, identifiers);
		assertThat(entry, Matchers.is(new Entry(identifiers,"JOptionPane#showMessageDialog(Component,Object)")));
	}
}
