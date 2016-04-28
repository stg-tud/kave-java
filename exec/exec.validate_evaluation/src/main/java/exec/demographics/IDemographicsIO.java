package exec.demographics;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.CompletionEvent;

public interface IDemographicsIO {
	Set<String> findUsers();
	Position getPosition(String user);
	int getNumQueries(String user);
	List<CompletionEvent> readEvents(String user);
}
