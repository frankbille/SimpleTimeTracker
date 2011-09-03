package dk.frankbille.simpletimetracker.dao;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import dk.frankbille.simpletimetracker.dao.PMF.PersistenceInvoker;
import dk.frankbille.simpletimetracker.domain.Account;

public class AccountDao {

	public static Account createNewAccount() {
		try {
			return PMF.doWithPersistenceManager(new PersistenceInvoker<Account>() {
				@Override
				public Account invoke(PersistenceManager persistenceManager) throws IOException {
					Account account = new Account();
					account.setCreated(new Date());
					account.setAccountKey(UUID.randomUUID().toString());
					
					persistenceManager.makePersistent(account);
					
					return account;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Account getAccount(final String accountKey) {
		try {
			return PMF.doWithPersistenceManager(new PersistenceInvoker<Account>() {
				@Override
				public Account invoke(PersistenceManager persistenceManager) throws IOException {
					try {
						return persistenceManager.getObjectById(Account.class, accountKey);
					} catch(JDOObjectNotFoundException e) {
						return null;
					}
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
