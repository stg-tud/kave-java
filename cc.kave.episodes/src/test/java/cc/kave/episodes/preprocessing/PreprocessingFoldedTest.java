package cc.kave.episodes.preprocessing;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.TrainingDataIO;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.events.Event;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingFoldedTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private IndivReposParser repoParser;
	@Mock
	private TrainingDataIO trainingIo;
	@Mock
	private ValidationDataIO validationIo;

	@Captor
	private ArgumentCaptor<List<Event>> trainingData;
	@Captor
	private ArgumentCaptor<List<Event>> validationData;

	private static final int FOLD_NUM = 10;

	private PreprocessingFolded sut;

	@Before
	public void setup() {
		initMocks(this);

		sut = new PreprocessingFolded(repoParser, trainingIo,
				validationIo);
	}
}
