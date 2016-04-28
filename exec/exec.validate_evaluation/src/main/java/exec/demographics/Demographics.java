package exec.demographics;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Demographics implements Iterable<Demographic> {

	private final Set<Demographic> demographics = new HashSet<>();
	
	public void add(Demographic demographic) {
		demographics.add(demographic);
	}
	
	public Set<Demographic> getDemographics() {
		return demographics;
	}

	@Override
	public Iterator<Demographic> iterator() {
		return demographics.iterator();
	}
	
	public int size() {
		return demographics.size();
	}
	
	public String toCSV() {
		StringBuilder csv = new StringBuilder();
		csv.append("user, position, first, last, days, months, events, queries, contribution");
		csv.append("\n");
		for (Demographic demographic : demographics) {
			csv.append(demographic.user).append(", ");
			csv.append(demographic.position.toString()).append(", ");
			csv.append(demographic.firstEventDate).append(", ");
			csv.append(demographic.lastEventDate).append(", ");
			csv.append(demographic.numberOfParticipationDays).append(", ");
			csv.append(demographic.numberOfParticipationMonths).append(", ");
			csv.append(demographic.numberOfCompletionEvents).append(", ");
			csv.append(demographic.numberOfGenQueries).append(", ");
			csv.append(demographic.getPercentageOfTotalQueries()).append("\n");
		}
		return csv.toString();
	}
	
	public void toCSVFile(File file) throws IOException {
		FileUtils.write(file, toCSV());
	}
}
