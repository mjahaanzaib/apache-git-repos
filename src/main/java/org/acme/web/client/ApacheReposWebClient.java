package org.acme.web.client;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import org.acme.dto.RepoInformation;
import org.acme.dto.UserInformation;
import org.acme.dto.ReleaseInformation;
import org.acme.dto.ContributorInformation;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class ApacheReposWebClient {

	private static final Logger logger = Logger.getLogger("ApacheReposWebClient");
	private static final String gitHubAccessToken = System.getProperty("githubToken");

	public List<RepoInformation> fnGetApacheReposByHttpClient() {
		int page = 1;
		int perPage = 100;
		boolean hasNext = true;
		List<RepoInformation> repoInformation = new ArrayList<>();
		while (hasNext) {
			try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
				String url = "https://api.github.com/orgs/apache/repos?page=" + page + "&per_page=" + perPage;
				logger.info("Calling webclient fnGetApacheReposByHttpClient() -> " + url);
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept", "application/vnd.github+json");
				httpGet.addHeader("Authorization",
						"Bearer " + gitHubAccessToken);
				httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					repoInformation.addAll(objectMapper.readValue(line, new TypeReference<List<RepoInformation>>() {
					}));
					fnAppendUsingPrintWriter("repos-response/repos_all_pages_info", repoInformation.toString());
				}
				hasNext = httpResponse.getFirstHeader("link").toString().contains("rel=\"next\"");
				logger.info("page_hasNext -> " + hasNext + " -> page -> " + page);
				page++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return repoInformation;
	}

	public ReleaseInformation fnGetApacheReposReleaseInfoByHttpClient(String repoName) {
		String url = "https://api.github.com/repos/apache/" + repoName + "/releases/latest";
		logger.info("Calling webclient fnGetApacheReposReleaseInfoByHttpClient() -> " + url);
		ReleaseInformation releaseInformation = new ReleaseInformation();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization",
					"Bearer " + gitHubAccessToken);
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			logger.info("webclient call status -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					releaseInformation = objectMapper.readValue(line, ReleaseInformation.class);
					logger.info(line);
					if (releaseInformation.getAssets().size() > 0) {
						fnAppendUsingPrintWriter("release-response/" + repoName, releaseInformation.toString());
						logger.info("nested: " + line);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return releaseInformation;
	}

	public List<ContributorInformation> fnGetReposContributor(String contributorUrl) {
		logger.info("Calling webclient fnGetReposContributor() -> " + contributorUrl);
		List<ContributorInformation> contributorInformationList = new ArrayList<>();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
			HttpGet httpGet = new HttpGet(contributorUrl);
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization",
					"Bearer " + gitHubAccessToken);
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			logger.info("webclient call status -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					contributorInformationList = objectMapper.readValue(line,
							new TypeReference<List<ContributorInformation>>() {
							});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contributorInformationList;
	}

	public UserInformation fnGetUserInformation(String user) {
		String url = "https://api.github.com/users/" + user;
		logger.info("Calling webclient fnGetUserInformation() -> " + url);
		UserInformation userInformation = new UserInformation();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization",
					"Bearer " + gitHubAccessToken);
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			logger.info("webclient call status -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					userInformation = objectMapper.readValue(line, UserInformation.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInformation;
	}

	private void fnAppendUsingPrintWriter(String filePath, String text) {
		File file = new File(filePath);
		FileWriter fr = null;
		BufferedWriter br = null;
		PrintWriter pr = null;
		try {
			// to append to file, you need to initialize FileWriter using below constructor
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			pr = new PrintWriter(br);
			pr.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pr.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
