package dk.frankbille.simpletimetracker.api.rest;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import dk.frankbille.simpletimetracker.dao.TimeDao;
import dk.frankbille.simpletimetracker.domain.TimeEntry;

public class TimeResource extends ServerResource {

	@Get
	public TimeEntry[] list() {
		String accountKey = getRequest().getChallengeResponse().getIdentifier();
		
		List<TimeEntry> entries = TimeDao.getTimeEntries(accountKey);
		
		return entries.toArray(new TimeEntry[entries.size()]);
	}
	
}
