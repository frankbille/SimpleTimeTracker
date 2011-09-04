package dk.frankbille.simpletimetracker.api.rest;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import dk.frankbille.simpletimetracker.dao.AccountDao;
import dk.frankbille.simpletimetracker.domain.Account;

public class CreateAccountResource extends ServerResource {
	
	@Post
	public Account createAccount() {
		Account account = AccountDao.createNewAccount();
		
		return account;
	}
	
}
