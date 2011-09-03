package dk.frankbille.simpletimetracker.dao;

import java.io.IOException;
import java.util.ArrayList;
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
					List<TimeEntry> entries = new ArrayList<TimeEntry>();
					Query query = persistenceManager.newQuery(TimeEntry.class);
					query.setFilter("accountKey == theAccountKey");
					query.declareParameters("String theAccountKey");
					List<TimeEntry> result = (List<TimeEntry>) query.execute(accountKey);
					entries.addAll(result);
					return entries;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static TimeEntry getLatestTimeEntry(final String accountKey) {
		try {
			return PMF.doWithPersistenceManager(new PersistenceInvoker<TimeEntry>() {
				@SuppressWarnings("unchecked")
				@Override
				public TimeEntry invoke(PersistenceManager persistenceManager) throws IOException {
					Query query = persistenceManager.newQuery(TimeEntry.class);
					query.setFilter("accountKey == theAccountKey");
					query.setOrdering("startTime DESC");
					query.declareParameters("String theAccountKey");
					query.setRange(0, 1);
					List<TimeEntry> result = (List<TimeEntry>) query.execute(accountKey);
					if (result.size() == 1) {
						return result.get(0);
					} else {
						return null;
					}
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void saveTimeEntry(final TimeEntry timeEntry) {
		try {
			PMF.doWithPersistenceManager(new PersistenceInvoker<Void>() {
				@Override
				public Void invoke(PersistenceManager persistenceManager) throws IOException {
					persistenceManager.makePersistent(timeEntry);
					return null;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
