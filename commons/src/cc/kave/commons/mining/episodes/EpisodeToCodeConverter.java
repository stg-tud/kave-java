package cc.kave.commons.mining.episodes;

import java.util.List;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.OrderedEventsInEpisode;

public class EpisodeToCodeConverter {

	public String LearnedPatternConverter(OrderedEventsInEpisode episode, List<Event> mappingList) {
		String codeRepresentation = "";

		if (!episode.getPartialOrderList().isEmpty()) {
			for (String eventID : episode.getPartialOrderList()) {
				try {
					codeRepresentation += mappingList.get(Integer.getInteger(eventID)).getMethod().getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!episode.getSequentialOrderList().isEmpty()) {
			for (String eventIDs : episode.getSequentialOrderList()) {
				if (eventIDs.length() == 1) {
					try {
						codeRepresentation += mappingList.get(Integer.getInteger(eventIDs)).getMethod().getName();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					for (int i = 0; i < eventIDs.length(); i++) {
						char id = eventIDs.charAt(i);
					}
				}
			}
		}
		return codeRepresentation;
	}
}
