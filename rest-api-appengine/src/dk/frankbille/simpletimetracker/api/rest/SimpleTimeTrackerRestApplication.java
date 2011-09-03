package dk.frankbille.simpletimetracker.api.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

public class SimpleTimeTrackerRestApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router root = new Router(getContext());
		
		// Non-authenticated API calls
		root.attach("/createaccount", CreateAccountResource.class);
		
		// Authenticated API calls
		ChallengeAuthenticator secureRouter = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "SimpleTimeTracker");
		secureRouter.setVerifier(new AccountVerifier());
		
		Router securedRouter = new Router(getContext());
		securedRouter.attach("/timer", TimerResource.class);
		securedRouter.attach("/time", TimeResource.class);
		secureRouter.setNext(securedRouter);

		root.attach(secureRouter);
		
		return root;
	}
	
}
