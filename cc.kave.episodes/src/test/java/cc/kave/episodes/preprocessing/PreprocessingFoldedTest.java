package cc.kave.episodes.preprocessing;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.episodes.io.IndivReposParser;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingFoldedTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private IndivReposParser repoParser;
	
	private static final int FOLD_NUM = 10;
}
