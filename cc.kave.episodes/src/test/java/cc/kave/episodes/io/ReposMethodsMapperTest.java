package cc.kave.episodes.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;

public class ReposMethodsMapperTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Map<String, List<Event>> mapper;

	private RepoMethodsMapperIO sut;

	@Before
	public void setup() throws ZipException, IOException {
		initMocks(this);

		mapper = generateMapper();

		sut = new RepoMethodsMapperIO(tmp.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories folder does not exist");
		sut = new RepoMethodsMapperIO(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories is not a folder, but a file");
		sut = new RepoMethodsMapperIO(file);
	}

	@Test
	public void fileIsCreated() throws ZipException, IOException {
		File file = new File(getReposMethodsPath());

		sut.writer(mapper);

		assertTrue(file.exists());
	}

	@Test
	public void fileContent() throws ZipException, IOException {
		sut.writer(mapper);

		@SuppressWarnings("serial")
		Type type = new TypeToken<Map<String, List<Event>>>() {
		}.getType();
		Map<String, List<Event>> actMapper = JsonUtils.fromJson(new File(
				getReposMethodsPath()), type);

		assertEquals(generateMapper(), actMapper);
	}

	private String getReposMethodsPath() {
		String path = tmp.getRoot().getAbsolutePath()
				+ "/repoMethodsMapper.json";
		return path;
	}

	private Map<String, List<Event>> generateMapper() {
		Map<String, List<Event>> mapper = Maps.newLinkedHashMap();
		mapper.put("Github/usr1/repo1", Lists.newArrayList(enclCtx(1)));
		mapper.put("Github/usr1/repo2", Lists.newArrayList(enclCtx(2)));
		mapper.put("Github/usr1/repo3", Lists.newArrayList(enclCtx(3)));

		return mapper;
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}
}
