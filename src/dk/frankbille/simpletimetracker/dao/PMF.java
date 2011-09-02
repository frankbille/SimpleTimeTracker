package dk.frankbille.simpletimetracker.dao;

import java.io.IOException;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
	}

	private static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static interface PersistenceInvoker<T> {
		T invoke(PersistenceManager persistenceManager) throws IOException;
	}

	public static <T> T doWithPersistenceManager(
			PersistenceInvoker<T> persistenceInvoker) throws IOException {
		T returnValue = null;

		PersistenceManager persistenceManager = PMF.get()
				.getPersistenceManager();
		try {
			returnValue = persistenceInvoker.invoke(persistenceManager);
		} finally {
			persistenceManager.close();
		}

		return returnValue;
	}

}
