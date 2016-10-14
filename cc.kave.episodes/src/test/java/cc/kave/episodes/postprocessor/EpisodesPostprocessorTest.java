/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class EpisodesPostprocessorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private EpisodeParser parser;
	
	private static final int NUMBREPOS = 2;
	private static final int FREQTHRESH = 5;
	private static final double BIDIRECTTHRESH = 0.5;
	
	private Map<Integer, Set<Episode>> episodes;
	
	EpisodesPostprocessor sut;
	
	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);
		
		episodes = Maps.newLinkedHashMap();
		
		sut = new EpisodesPostprocessor(parser);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
	}
	
	@Test
	public void removeOneNodes() throws Exception {
		Set<Episode> oneNodes = Sets.newLinkedHashSet();
		
		oneNodes.add(createEpisode(3, 0.7, "1"));
		oneNodes.add(createEpisode(8, 0.5, "2"));
		episodes.put(1, oneNodes);
		
		Map<Integer, Set<Episode>> actuals = sut.postprocess(NUMBREPOS, FREQTHRESH, BIDIRECTTHRESH);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
		
		verify(parser).parse(eq(NUMBREPOS));
		
		assertEquals(Maps.newLinkedHashMap(), actuals);
	}
	
	@Ignore
	@Test
	public void multipleEqualEpisodes() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("There are two episodes exactly the same!");
		
		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		twoNodes.add(createEpisode(8, 0.6, "1", "2", "2>1"));
		episodes.put(2, twoNodes);
		
		sut.postprocess(NUMBREPOS, FREQTHRESH, BIDIRECTTHRESH);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
		
		verify(parser).parse(eq(NUMBREPOS));
	}
	
	@Test
	public void freqRepr() throws Exception {
		
		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		twoNodes.add(createEpisode(9, 0.6, "1", "2", "2>1"));
		twoNodes.add(createEpisode(8, 0.6, "1", "2"));
		episodes.put(2, twoNodes);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(9, 0.6, "1", "2", "2>1"));
		expected.put(2, set);
		
		Map<Integer, Set<Episode>> actuals = sut.postprocess(NUMBREPOS, FREQTHRESH, BIDIRECTTHRESH);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
		
		verify(parser).parse(eq(NUMBREPOS));
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void bidirectRepr() throws Exception {
		
		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		twoNodes.add(createEpisode(8, 0.8, "1", "2", "2>1"));
		twoNodes.add(createEpisode(8, 0.7, "1", "2"));
		twoNodes.add(createEpisode(6, 0.8, "1", "3", "3>1"));
		twoNodes.add(createEpisode(6, 0.7, "1", "3", "1>3"));
		twoNodes.add(createEpisode(6, 0.5, "1", "3"));
		episodes.put(2, twoNodes);
		
		Set<Episode> threeNodes = Sets.newLinkedHashSet();
		threeNodes.add(createEpisode(8, 0.6, "1", "2", "3", "1>2", "1>3"));
		threeNodes.add(createEpisode(8, 0.8, "1", "2", "3", "2>1", "2>3"));
		threeNodes.add(createEpisode(8, 0.7, "1", "2", "3", "3>1", "3>2"));
		threeNodes.add(createEpisode(8, 0.5, "1", "2", "3"));
		episodes.put(3, threeNodes);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set2 = Sets.newLinkedHashSet();
		set2.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		set2.add(createEpisode(6, 0.5, "1", "3"));
		expected.put(2, set2);
		
		Set<Episode> set3 = Sets.newLinkedHashSet();
		set3.add(createEpisode(8, 0.5, "1", "2", "3"));
		expected.put(3, set3);
		
		Map<Integer, Set<Episode>> actuals = sut.postprocess(NUMBREPOS, FREQTHRESH, BIDIRECTTHRESH);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
		
		verify(parser).parse(eq(NUMBREPOS));
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void multipleNodes() throws Exception {
		
		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		twoNodes.add(createEpisode(5, 0.2, "1", "3", "1>3"));
		twoNodes.add(createEpisode(3, 0.6, "1", "2", "1>2"));
		twoNodes.add(createEpisode(5, 0.8, "2", "3", "2>3"));
		twoNodes.add(createEpisode(8, 0.8, "1", "2", "2>1"));
		twoNodes.add(createEpisode(8, 0.7, "1", "2"));
		episodes.put(2, twoNodes);
		
		Set<Episode> threeNodes = Sets.newLinkedHashSet();
		threeNodes = Sets.newLinkedHashSet();
		threeNodes.add(createEpisode(7, 0.9, "1", "2", "3", "1>2", "1>3"));
		threeNodes.add(createEpisode(3, 1.0, "1", "3", "4", "1>3", "1>4"));
		threeNodes.add(createEpisode(6, 0.9, "1", "2", "3"));
		episodes.put(3, threeNodes);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set2 = Sets.newLinkedHashSet();
		set2.add(createEpisode(8, 0.6, "1", "2", "1>2"));
		set2.add(createEpisode(5, 0.8, "2", "3", "2>3"));
		expected.put(2, set2);
		
		Set<Episode> set3 = Sets.newLinkedHashSet();
		set3.add(createEpisode(7, 0.9, "1", "2", "3", "1>2", "1>3"));
		expected.put(3, set3);
		
		Map<Integer, Set<Episode>> actuals = sut.postprocess(NUMBREPOS, FREQTHRESH, BIDIRECTTHRESH);
		
		when(parser.parse(anyInt())).thenReturn(episodes);
		
		verify(parser).parse(eq(NUMBREPOS));
		
		assertEquals(expected, actuals);
	}
	
	private Episode createEpisode(int freq, double bdmeas, String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setBidirectMeasure(bdmeas);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	} 
}
