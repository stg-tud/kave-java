package exec.demographics;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class DemographicsTest {

	@Test
	public void toCSV() {
		Demographic demographic1 = new Demographic();
		demographic1.user = "user1";
		demographic1.position = Position.Academic;
		demographic1.firstEventDate = LocalDate.of(1942, 5, 23);
		demographic1.lastEventDate = LocalDate.of(2016, 4, 28);
		demographic1.numberOfCompletionEvents = 5;
		demographic1.numberOfGenQueries = 666;
		demographic1.totalNumberOfGenQueries = 1037;
		demographic1.numberOfParticipationDays = 2;
		demographic1.numberOfParticipationMonths = 1;
		
		Demographic demographic2 = new Demographic();
		demographic2.user = "user2";
		demographic2.position = Position.Industry;
		demographic2.firstEventDate = LocalDate.of(2012, 1, 4);
		demographic2.lastEventDate = LocalDate.of(2013, 3, 31);
		demographic2.numberOfCompletionEvents = 99;
		demographic2.numberOfGenQueries = 0;
		demographic2.totalNumberOfGenQueries = 50;
		demographic2.numberOfParticipationDays = 12;
		demographic2.numberOfParticipationMonths = 3;
		
		Demographics demographics = new Demographics();
		demographics.add(demographic1);
		demographics.add(demographic2);
		
		String csv = demographics.toCSV();
		
		assertThat(csv, is(
				"user, position, first, last, days, months, events, queries, contribution\n" +
				"user1, Academic, 1942-05-23, 2016-04-28, 2, 1, 5, 666, 0.6422372227579557\n" +
				"user2, Industry, 2012-01-04, 2013-03-31, 12, 3, 99, 0, 0.0\n"));
	}
}
