package exec.recommender_reimplementation.heinemann_analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ExtractionUtilTest {

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
}
