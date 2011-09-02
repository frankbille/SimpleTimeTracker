package dk.frankbille.simpletimetracker.api.rest;

import org.restlet.security.SecretVerifier;

import dk.frankbille.simpletimetracker.dao.AccountDao;
import dk.frankbille.simpletimetracker.domain.Account;

public class AccountVerifier extends SecretVerifier {

	@Override
	public boolean verify(String identifier, char[] secret) {
		Account account = AccountDao.getAccount(identifier);
		
		if (account != null) {
			return true;
		} else {
			return false;
		}
	}

}
