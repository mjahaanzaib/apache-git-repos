package org.acme.service;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.logging.Logger;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.List;

import org.acme.web.client.ApacheReposWebClient;
import org.h2.jdbcx.JdbcDataSource;
import org.acme.dto.ContributorInformation;
import org.acme.dto.ReleaseInformation;
import org.acme.dto.RepoInformation;
import org.acme.dto.UserInformation;
import org.acme.dto.Response;

public class ApacheReposService {
	private static final Logger logger = Logger.getLogger("ApacheReposService");

	public List<RepoInformation> getApacheReposList() {
		logger.info("Getting repos information list...");
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		List<RepoInformation> reposInformation = apacheReposWebClient.fnGetApacheReposByHttpClient();
		return reposInformation;
	}

	public List<Response> getTopFiveReposWithMostDownloads() {
		logger.info("Getting top 5 most downloads repos...");
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		int count = 0;
		ArrayList<Response> responseList = new ArrayList<>();
		// for (RepoInformation repoInformation : getApacheReposList()) {
		// logger.info("" + count++);
		// Response response = new Response();
		// ReleaseInformation releaseInformation = apacheReposWebClient
		// .fnGetApacheReposReleaseInfoByHttpClient(repoInformation.getName());
		// if (!Objects.isNull(releaseInformation)) {
		// response = new Response(repoInformation.getName(),
		// releaseInformation.getId(),
		// releaseInformation.getTagName(),
		// releaseInformation.getAssets().get(0).getDownloadCount(),
		// repoInformation.getContributorsUrl());
		// responseList.add(response);
		// }
		// }

		responseList.add(
				new Response("xyz", 521, "xoxo", 12,
						"https://api.github.com/repos/apache/tapestry3/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 111,
						"https://api.github.com/repos/apache/maven-mvnd/contributors"));
		responseList.add(new Response("third", 521, "xoxo", 12232,
				"https://api.github.com/repos/apache/predictionio/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 123,
						"https://api.github.com/repos/apache/flink-cdc/contributors"));
		responseList.add(new Response("xyz", 521, "xoxo", 112,
				"https://api.github.com/repos/apache/tvm/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 1299,
						"https://api.github.com/repos/apache/streampipes/contributors"));
		responseList.add(new Response("first", 521, "xoxo", 1299999,
				"https://api.github.com/repos/apache/superset/contributors"));
		responseList.add(new Response("second", 521, "xoxo", 1289898,
				"https://api.github.com/repos/apache/camel-k/contributors"));
		responseList.add(new Response("xyz", 521, "xoxo", 13,
				"https://api.github.com/repos/apache/hop/contributors"));
		responseList
				.add(new Response("xyz", 521, "xoxo", 189,
						"https://api.github.com/repos/apache/geode/contributors"));
		responseList
				.add(new Response("xyz", 521, "xoxo", 120,
						"https://api.github.com/repos/apache/xalan-c/contributors"));
		Collections.sort(responseList);

		logger.info("hashtable -> " + responseList.size());

		List<Response> top5mostDownloadsRepos = responseList.subList(0, Math.min(responseList.size(), 5));
		logger.info("Five Repos sorted by most downloads -> " + top5mostDownloadsRepos.toString());

		return top5mostDownloadsRepos;
	}

	public Hashtable<String, List<ContributorInformation>> fnGetTopTenReposContributor() {
		logger.info("Getting top 10 repo contributors information list...");
		List<Response> topFiveRepos = getTopFiveReposWithMostDownloads();
		Hashtable<String, List<ContributorInformation>> contributorsTableofRepo = new Hashtable<String, List<ContributorInformation>>();
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		for (Response response : topFiveRepos) {
			List<ContributorInformation> contributorInformationList = new ArrayList<ContributorInformation>();
			contributorInformationList = apacheReposWebClient.fnGetReposContributor(response.getContributorUrl());
			Collections.sort(contributorInformationList);
			List<ContributorInformation> top10ContributorsInfo = contributorInformationList.subList(0,
					Math.min(contributorInformationList.size(), 10));
			contributorsTableofRepo.put(response.getRepoName(), top10ContributorsInfo);
		}
		logger.info(" Top 10 contributors of repo sorted by most commits -> " + contributorsTableofRepo.toString());
		return contributorsTableofRepo;
	}

	public void fnSaveContributorInfo() {
		logger.info("Storing top 10 contributors of repo in h2 db...");
		Hashtable<String, List<ContributorInformation>> contributorsTableofRepo = fnGetTopTenReposContributor();
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		for (Entry<String, List<ContributorInformation>> repoTable : contributorsTableofRepo.entrySet()) {
			for (ContributorInformation contributorInformation : repoTable.getValue()) {
				UserInformation userInformation = apacheReposWebClient
						.fnGetUserInformation(contributorInformation.getLogin());
				userInformation.setRepoName(repoTable.getKey());
				userInformation.setCommitCount(contributorInformation.getContributions());
				logger.info("userInformation -> " + userInformation.toString());
				try {
					fnSaveInfoInH2Database(userInformation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void fnSaveInfoInH2Database(UserInformation userInformation) {
		logger.info("Saving info in h2 database....");
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:file:./h2/apache_github_data");
		String createTableQuery = "CREATE TABLE IF NOT EXISTS contributor_info (" +
				"id int auto_increment primary key," +
				"repository_name VARCHAR(255)," +
				"contributor_username VARCHAR(255)," +
				"contributor_name VARCHAR(255)," +
				"contributor_location VARCHAR(255)," +
				"contributor_company VARCHAR(255)," +
				"commit_count int" +
				");";
		try (var connection = dataSource.getConnection()) {
			connection.createStatement().execute(createTableQuery);
			fnSaveContributors(connection, userInformation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fnSaveContributors(Connection connection, UserInformation userInformation) throws SQLException {

		logger.info("Calling saveContributors()....");

		String contributorInfo = "INSERT INTO contributor_info (repository_name, contributor_username, contributor_name, contributor_location, contributor_company, commit_count) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(contributorInfo)) {
			preparedStatement.setString(1, userInformation.getRepoName());
			preparedStatement.setString(2, userInformation.getLogin());
			preparedStatement.setString(3, userInformation.getName());
			preparedStatement.setString(4, userInformation.getLocation());
			preparedStatement.setString(5, userInformation.getCompany());
			preparedStatement.setLong(6, userInformation.getCommitCount());

			logger.info("Insertion status -> " + preparedStatement.executeUpdate());
			if (preparedStatement.executeUpdate() == 1) {
				logger.info("Saved...");
			} else {
				logger.info("Not saved...");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fnGetContributionsInfoFromH2() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setUrl("jdbc:h2:file:./h2/apache_github_data");
		String contributorInfoSelect = "SELECT repository_name, contributor_username, contributor_name, contributor_location, contributor_company, commit_count FROM contributor_info";
		try (
				Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(contributorInfoSelect);) {

			logger.info("============ Stored Info In H2 ============");
			while (rs.next()) {
				// Retrieve by column name
				String repositoryName = rs.getString("repository_name");
				String contributorUsername = rs.getString("contributor_username");
				String contributorName = rs.getString("contributor_name");
				String contributorLocation = rs.getString("contributor_location");
				String contributorCompany = rs.getString("contributor_company");
				long commitCount = rs.getLong("commit_count");

				logger.info("repository_name : " + repositoryName + " | contributor_username : " + contributorUsername
						+ " | contributor_name : " + contributorName + " | contributor_location : "
						+ contributorLocation + " | contributor_company : " + contributorCompany + " | commit_count : "
						+ commitCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}