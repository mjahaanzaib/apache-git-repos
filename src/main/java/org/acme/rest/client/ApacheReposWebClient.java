package org.acme.rest.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.acme.dto.ContributorInformation;
import org.acme.dto.ReleaseInformation;
import org.acme.dto.RepoInformation;
import org.acme.dto.UserInformation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApacheReposWebClient {

	public List<RepoInformation> getApacheReposByHttpClient() {

		int page = 1;
		int perPage = 100;
		boolean hasNext = true;
		List<RepoInformation> repoInformation = new ArrayList<>();

		while (hasNext) {
			try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {

				HttpGet httpGet = new HttpGet(
						"https://api.github.com/orgs/apache/repos?page=" + page + "&per_page=" + perPage);
				httpGet.addHeader("Accept", "application/vnd.github+json");
				httpGet.addHeader("Authorization", "Bearer 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");
				httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");

				HttpResponse httpResponse = httpClient.execute(httpGet);
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					repoInformation.addAll(objectMapper.readValue(line, new TypeReference<List<RepoInformation>>() {
					}));
					appendUsingPrintWriter(
							"repos-response/repo_page_" + page,
							repoInformation.toString());
				}
				hasNext = httpResponse.getFirstHeader("link").toString().contains("rel=\"next\"");
				System.out.println(httpResponse.getFirstHeader("link").toString() + " hasNext -> " + hasNext
						+ " -> page -> " + page);
				page++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return repoInformation;
	}

	public ReleaseInformation getApacheReposReleaseInfoByHttpClient(String repoName) {

		ReleaseInformation releaseInformation = new ReleaseInformation();

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {

			HttpGet httpGet = new HttpGet("https://api.github.com/repos/apache/" + repoName + "/releases/latest");
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization", "Bearer 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");
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			System.out.println("release-url -> https://api.github.com/repos/apache/" + repoName + "/releases/latest");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			System.out.println("release call -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("release called -> " + httpResponse.getStatusLine().getStatusCode());
				BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				String line = "";
				if ((line = br.readLine()) != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					releaseInformation = objectMapper.readValue(line, ReleaseInformation.class);
					System.out.println(line);
					if (releaseInformation.getAssets().size() > 0) {
						appendUsingPrintWriter(
								"release-response/" + repoName,
								releaseInformation.toString());
						System.out.println("nested: " + line);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return releaseInformation;
	}

	public List<ContributorInformation> getReposContributor(String contributorUrl) {
		System.out.println("calling getReposContributor() -> "+ contributorUrl);
		List<ContributorInformation> contributorInformationList = new ArrayList<>();

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {

			HttpGet httpGet = new HttpGet(contributorUrl);
			System.out.println("client url -> " + contributorUrl);
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization", "Bearer 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");
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			System.out.println("release call -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("release called -> " + httpResponse.getStatusLine().getStatusCode());
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

	public UserInformation getUserInformation(String user) {
		UserInformation userInformation = new UserInformation();
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
			HttpGet httpGet = new HttpGet("https://api.github.com/users/" + user);
			httpGet.addHeader("Accept", "application/vnd.github+json");
			httpGet.addHeader("Authorization", "Bearer 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");
			httpGet.addHeader("X-GitHub-Api-Version", "2022-11-28");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			System.out.println("release call -> " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("release called -> " + httpResponse.getStatusLine().getStatusCode());
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

	private void appendUsingPrintWriter(String filePath, String text) {
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
