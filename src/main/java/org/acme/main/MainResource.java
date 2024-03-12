package org.acme.main;

import java.util.logging.Logger;

import org.acme.service.ApacheReposService;

public class MainResource {

	private static final Logger logger = Logger.getLogger("MainResource");
	private static String gitHubAccessToken = System.getProperty("githubToken");

	public static void main(String[] args) {
		if (gitHubAccessToken != null) {
			logger.info("========= Currenlty we are using your's token to authentication for github api =========");
			ApacheReposService apacheReposService = new ApacheReposService(gitHubAccessToken);
			logger.info(
					"========= Kindly wait for a while to gather the required information from apache repositories =========");
			apacheReposService.fnSaveContributorInfo();
			logger.info("========= Required information from apache repositories has been stored in H2 =========");

			logger.info("========= Try to retrieve apache repositories information from H2 =========");
			apacheReposService.fnGetContributionsInfoFromH2();
		} else {
			logger.info("Kindly update access token in .batch file.");
		}
	}
}