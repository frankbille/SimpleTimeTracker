package dk.frankbille.simpletimetracker.api.rest;

import java.util.Date;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import dk.frankbille.simpletimetracker.dao.TimeDao;
import dk.frankbille.simpletimetracker.domain.TimeEntry;

public class TimerResource extends ServerResource {

	@Post
	public TimeEntry toggle() {
		String accountKey = getRequest().getChallengeResponse().getIdentifier();
		
		TimeEntry timeEntry = TimeDao.getLatestTimeEntry(accountKey);
		if (timeEntry == null || timeEntry.getEndTime() != null) {
			timeEntry = new TimeEntry();
			timeEntry.setStartTime(new Date());
			timeEntry.setAccountKey(accountKey);
		} else {
			timeEntry.setEndTime(new Date());
		}
		
		TimeDao.saveTimeEntry(timeEntry);
		
		return timeEntry;
	}
	
}
