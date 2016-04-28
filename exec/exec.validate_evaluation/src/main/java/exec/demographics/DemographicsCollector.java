package exec.demographics;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import cc.kave.commons.model.events.completionevents.CompletionEvent;

public class DemographicsCollector {

	private IDemographicsIO demographicsIO;

	public DemographicsCollector(IDemographicsIO demographicsIO) {
		this.demographicsIO = demographicsIO;
	}

	public Demographics collect() {
		Demographics demographics = new Demographics();
		int totalNumberOfGenQueries = 0;

		for (String user : demographicsIO.findUsers()) {
			Demographic demographic = collect(user);
			demographics.add(demographic);
			totalNumberOfGenQueries += demographic.numberOfGenQueries;
		}

		for (Demographic demographic : demographics) {
			demographic.totalNumberOfGenQueries = totalNumberOfGenQueries;
		}

		return demographics;
	}

	private Demographic collect(String user) {
		Demographic demographic = new Demographic();
		demographic.user = user;
		demographic.position = demographicsIO.getPosition(user);
		demographic.numberOfGenQueries = demographicsIO.getNumQueries(user);
		List<CompletionEvent> events = demographicsIO.readEvents(user);
		demographic.numberOfCompletionEvents = events.size();
		demographic.numberOfParticipationDays = countUnique(events, this::getDate);
		demographic.numberOfParticipationMonths = countUnique(events, this::getMonth);
		if (events.size() > 0) {
			demographic.firstEventDate = getDate(getFirst(events));
			demographic.lastEventDate = getDate(getLast(events));
		}
		return demographic;
	}

	private CompletionEvent getLast(List<CompletionEvent> events) {
		return events.get(events.size() - 1);
	}

	private CompletionEvent getFirst(List<CompletionEvent> events) {
		return events.get(0);
	}
	
	private <T> int countUnique(Collection<CompletionEvent> events, Function<CompletionEvent, T> unit) {
		return (int) events.stream().map(unit).distinct().count();
	}

	private LocalDate getMonth(CompletionEvent event) {
		return getDate(event).withDayOfMonth(1);
	}

	private LocalDate getDate(CompletionEvent event) {
		return event.TriggeredAt.toLocalDate();
	}
}
