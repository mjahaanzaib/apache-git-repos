package org.acme.web;

// import java.util.List;

// import org.acme.dto.ReleaseInformation;
// import org.acme.dto.RepoInformation;
import org.acme.service.ApacheReposService;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;

// import jakarta.ws.rs.GET;
// import jakarta.ws.rs.Path;
// import jakarta.inject.Inject;
// import jakarta.ws.rs.Produces;
// import jakarta.ws.rs.PathParam;
// import jakarta.ws.rs.core.Response;
// import jakarta.ws.rs.core.MediaType;

// @Path("/users")
public class MainResource {

	private static final Logger logger = Logger.getLogger(MainResource.class);
	public static void main(String[] args) {
		logger.info("========= Kindly wait for a while to gather the required information from apache repositories =========");
		ApacheReposService apacheReposService = new ApacheReposService();
		// apacheReposService.fnSaveContributorInfo();
		logger.info("========= Required information from apache repositories has been stored in H2 =========");
	}
}