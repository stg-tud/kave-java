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
					codeRepresentation += mappingList.get(Integer.getInteger(eventID)).getMethod().getName() + "\n";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		codeRepresentation += "\n";
		if (!episode.getSequentialOrderList().isEmpty()) {
			for (String partialEventIDs : episode.getSequentialOrderList()) {
				String[] eventIds = partialEventIDs.split(" ");
				if (eventIds.length == 1) {
					try {
						codeRepresentation += mappingList.get(Integer.getInteger(eventIds[0])).getMethod().getName();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// for (int i = 0; i < eventIDs.length(); i++) {
					// char id = eventIDs.charAt(i);
					// }
				}
			}
		}
		return codeRepresentation;
	}
}
