package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class ExtractionUtilTest extends PBNAnalysisBaseTest {

	@Test
	public void camelSplitTwoWords() {
		assertTokenization("HelloWorld", "Hello", "World");
	}
	
	@Test
	public void camelSplitAcronym() {
		assertTokenization("SSTParser", "SST", "Parser");
	}
	
	@Test
	public void camelSplitMultipleWords() {
		assertTokenization("AGoodDay", "A", "Good", "Day");
	}
	
	private static void assertTokenization(String input, String... expecteds) {
		String[] actuals = ExtractionUtil.camelCaseSplit(input);
		assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void camelCaseSplitJavaTokens() {
		Set<String> identifiers = Sets.newHashSet("IOException", "String", "errorMessage", "e", "getMessage");
		Set<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertThat(splitIdentifiers, Matchers.containsInAnyOrder("io", "exception", "string", "error", "message", "e", "get"));
	}
	
	@Test
	public void camelCaseSplitCSharpTokens() {
		Set<String> identifiers = Sets.newHashSet("IOException", "String", "errorMessage", "e", "GetMessage");
		Set<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertThat(splitIdentifiers, Matchers.containsInAnyOrder("io", "exception", "string", "error", "message", "e", "get"));
	}
	
	@Test
	public void emptyListCamelCaseSplit() {
		Set<String> identifiers = new HashSet<>();
		Set<String> splitIdentifiers = ExtractionUtil.camelCaseSplitTokens(identifiers);
		assertTrue(splitIdentifiers.isEmpty());
	}
	
	@Test
	public void stemsTokens() {
		Set<String> identifiers = Sets.newHashSet("exception", "get", "message");
		Set<String> stemmedIdentifiers = ExtractionUtil.stemTokens(identifiers);
		assertThat(stemmedIdentifiers, Matchers.containsInAnyOrder("except", "get", "messag"));
	}
	
	@Test
	public void noDuplicatesAfterStemming() {
		Set<String> identifiers = Sets.newHashSet("message", "e", "get", "message");
		Set<String> stemmedIdentifiers = ExtractionUtil.stemTokens(identifiers);
		assertThat(stemmedIdentifiers, Matchers.containsInAnyOrder("messag","e", "get"));
	}
	
	@Test
	public void collectTokenTest() {
		List<String> tokens = Lists.newArrayList("readFile", "<catch>", "IOException", "e", "String", "errorMessage", "e", "getMessage");
		Set<String> identifiers = ExtractionUtil.collectTokens(tokens, 5, false);
		assertThat(identifiers, Matchers.containsInAnyOrder("String", "errorMessage", "e", "getMessage"));
	}
	
	@Test
	public void filterSingleCharactersTest() {
		Set<String> identifiers = Sets.newHashSet("IOException", "String", "errorMessage", "e", "getMessage");
		ExtractionUtil.filterSingleCharacterIdentifier(identifiers);
		assertThat(identifiers, Matchers.containsInAnyOrder("IOException", "String", "errorMessage", "getMessage"));
	}
	
	@Test
	public void isStopWordTest() {
		assertTrue(ExtractionUtil.isStopWord("a"));
	}
	
	@Test
	public void createsNewEntry() {
		Set<String> identifiers = Sets.newHashSet("io", "except", "string", "error", "message", "get");
		IMethodName methodName = method(voidType, type("JOptionPane"), "showMessageDialog", parameter(type("Component"), "component"), parameter(objectType, "message"));
		Entry entry = ExtractionUtil.createNewEntry(methodName, identifiers);
		assertThat(entry, Matchers.is(new Entry(identifiers,methodName)));
	}
}
