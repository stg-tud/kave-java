package cc.kave.episodes.data;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static cc.recommenders.io.LoggerUtils.assertLogContains;

import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class DataCounterTest {

	@Mock
	private Directory rootDirectory;
	
	private static final String REPO0 = "Github/usr1/repo0/ws.zip";
	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
	private static final String REPO2 = "Github/usr1/repo2/ws.zip";
	private static final String REPO3 = "Github/usr1/repo3/ws.zip";
	
	Set<String> repos;
	
	private DataCounter sut;
	
	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);
		
		repos = Sets.newLinkedHashSet();
		repos.add(REPO0);
		repos.add(REPO1);
		repos.add(REPO2);
		repos.add(REPO3);
		
		sut = new DataCounter(rootDirectory);
		
		when(rootDirectory.findFiles(anyPredicateOf(String.class))).thenAnswer(
				new Answer<Set<String>>() {
					@Override
					public Set<String> answer(InvocationOnMock invocation)
							throws Throwable {
						return repos;
					}
				});
		Logger.setPrinting(false);
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void logger() {
		Logger.clearLog();
		
		sut.namespaces();
		
		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
		
		assertLogContains(0, "Number of namespaces: 4");
	}
	
	private <T> Predicate<T> anyPredicateOf(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Predicate<T> p = any(Predicate.class);
		return p;
	}
}
