package org.acme.main;

import java.util.logging.Logger;

import org.acme.service.ApacheReposService;

public class MainResource {

	private static final Logger logger = Logger.getLogger("MainResource");
	private static final String gitHubAccessToken = System.getProperty("githubToken");

	public static void main(String[] args) {

		if (gitHubAccessToken != null) {
			ApacheReposService apacheReposService = new ApacheReposService();
			logger.info(
					"========= Kindly wait for a while to gather the required information from apache repositories =========");
			apacheReposService = new ApacheReposService();
			apacheReposService.fnSaveContributorInfo();
			logger.info("========= Required information from apache repositories has been stored in H2 =========");

			logger.info("========= Try to retrieve apache repositories information from H2 =========");
			apacheReposService.fnGetContributionsInfoFromH2();

		} else {
			logger.info("Kindly provide your access token by CLI argument, like -DgithubToken=<YOUR_ACCESS_TOKEN>");
			return;
		}
	}
}