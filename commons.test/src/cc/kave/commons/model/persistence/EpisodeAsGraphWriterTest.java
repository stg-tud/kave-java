package cc.kave.commons.model.persistence;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.episodes.Fact;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeAsGraphWriterTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private DirectedGraph<Fact, DefaultEdge> graph;
	private EpisodeAsGraphWriter sut;

	@Before
	public void setup() {
		sut = new EpisodeAsGraphWriter(rootFolder.getRoot());
		
		graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);
		
		Fact f1 = new Fact("1");
		Fact f2 = new Fact("2");
		Fact f3 = new Fact("3");
		Fact f4 = new Fact("4");
		
		graph.addVertex(f1);
		graph.addVertex(f2);
		graph.addVertex(f3);
		graph.addVertex(f4);
		
		graph.addEdge(f1, f2);
		graph.addEdge(f1, f3);
		graph.addEdge(f1, f4);
		graph.addEdge(f2, f4);
		graph.addEdge(f3, f4);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder does not exist");
		sut = new EpisodeAsGraphWriter(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder is not a folder, but a file");
		sut = new EpisodeAsGraphWriter(file);
	}

	@Test
	public void writerDirectoryTest() throws IOException {
		sut.write(graph, 1);
		assertTrue(getFilePath(1).getParentFile().exists());
		assertTrue(getFilePath(1).exists());
	}
	
	private File getFilePath(int index) {
		String dirPath = rootFolder.getRoot().getAbsolutePath() + "/graphs/";
		new File(dirPath).mkdirs();
		File fileName = new File(dirPath + "/graph" + index + ".dot");
		return fileName;
	}
}
