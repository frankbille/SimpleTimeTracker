package dk.frankbille.simpletimetracker.dao;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import dk.frankbille.simpletimetracker.dao.PMF.PersistenceInvoker;
import dk.frankbille.simpletimetracker.domain.TimeEntry;

public class TimeDao {

	public static List<TimeEntry> getTimeEntries(final String accountKey) {
		try {
			return PMF.doWithPersistenceManager(new PersistenceInvoker<List<TimeEntry>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<TimeEntry> invoke(PersistenceManager persistenceManager) throws IOException {
					Query query = persistenceManager.newQuery(TimeEntry.class);
					query.setFilter("accountKey == theAccountKey");
					query.declareParameters("String theAccountKey");
					return (List<TimeEntry>) query.execute(accountKey);
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
