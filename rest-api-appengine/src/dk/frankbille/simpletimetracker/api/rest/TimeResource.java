package dk.frankbille.simpletimetracker.api.rest;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import dk.frankbille.simpletimetracker.dao.TimeDao;
import dk.frankbille.simpletimetracker.domain.TimeEntry;

public class TimeResource extends ServerResource {

	@Get
	public TimeEntry latest() {
		String accountKey = getRequest().getChallengeResponse().getIdentifier();
		
		return TimeDao.getLatestTimeEntry(accountKey);
	}
	
}
