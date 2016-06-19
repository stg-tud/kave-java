package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.typeshapes.TypeShape;

import com.google.common.collect.Lists;

import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationSettings;

public class HeinemannTokenizationVisitorTest extends PBNAnalysisBaseTest {

	private HeinemannTokenizationVisitor uut;
	private TokenizationContext tokenizationContext;
	
	@Before
	public void setup() {
		uut = new HeinemannTokenizationVisitor(new TypeShape(), 5, false);
		TokenizationSettings settings = new TokenizationSettings();
		settings.setActiveBrackets(false);
		settings.setActiveKeywords(true);
		settings.setActiveOperators(false);
		settings.setActiveWrapKeywords(true);
		settings.setActivePuncutuation(false);
		
		tokenizationContext = new TokenizationContext(settings);
	}
	
	@Test
	public void collectTokenTest() {
		List<String> tokens = Lists.newArrayList("readFile", "<catch>", "IOException", "e", "String", "errorMessage", "e", "getMessage");
		List<String> identifiers = uut.collectTokens(tokens, 5);
		assertThat(identifiers, Matchers.contains("String", "errorMessage", "e", "getMessage"));
	}
	
	@Test
	public void filterSingleCharactersTest() {
		List<String> identifiers = Lists.newArrayList("IOException", "String", "errorMessage", "e", "getMessage");
		uut.filterSingleCharacterIdentifier(identifiers);
		assertThat(identifiers, Matchers.contains("IOException", "String", "errorMessage", "getMessage"));
	}
	
	@Test
	public void isStopWordTest() {
		assertTrue(uut.isStopWord("a"));
	}
	
	@Test
	public void createsAndStoresNewEntry() {
		List<String> identifiers = Lists.newArrayList("io", "except", "string", "error", "message", "get");
		IMethodName methodName = method(voidType, type("JOptionPane"), "showMessageDialog", parameter(type("Component"), "component"), parameter(objectType, "message"));
		uut.createAndStoreNewEntry(methodName, identifiers);
		assertThat(uut.getIndex(), Matchers.contains(new Entry(identifiers,"JOptionPane#showMessageDialog(Component,Object)")));
	}
	
	@Test
	public void onlyIdentifiersInSameMethodBody() {
		ISST sst = sst(DefaultClassContext, 
			methodDecl(DefaultMethodContext, true, 
					varDecl("someVar", stringType),
					assign("someVar", constant("..."))),
			methodDecl(method(voidType, DefaultClassContext, "m2"), true,
					varDecl("bar", stringType),
					varDecl("errorMessage",stringType),
					assign("errorMessage", referenceExpr(varRef("bar"))),
					invokeStmt("foo", DefaultMethodContext)));		
					
		sst.accept(uut, tokenizationContext);
		
		assertThat(uut.getIndex(),Matchers.contains(new Entry(Lists.newArrayList("string", "error","messag","bar"), "TDecl#M()")));
	}
		
	@Test
	public void noEntryWhenNotEnoughIdentifiers() {
		IMethodDeclaration methodDecl = methodDecl(method(voidType, DefaultClassContext, "m2"), true,
				invokeStmt("foo", DefaultMethodContext));
		
		methodDecl.accept(uut, tokenizationContext);
		
		assertTrue(uut.getIndex().isEmpty());
	}

}
