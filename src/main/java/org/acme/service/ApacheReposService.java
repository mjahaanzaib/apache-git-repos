package org.acme.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import org.acme.dto.ContributorInformation;
import org.acme.dto.ReleaseInformation;
import org.acme.dto.RepoInformation;
import org.acme.dto.Response;
import org.acme.dto.UserInformation;
import org.acme.rest.client.ApacheReposWebClient;
import org.acme.web.MainResource;
import org.h2.jdbcx.JdbcDataSource;
import org.jboss.logging.Logger;

public class ApacheReposService {

	private static final Logger logger = Logger.getLogger(MainResource.class);

	public List<RepoInformation> getApacheReposList() {
		System.out.println("getting repos information...");
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		List<RepoInformation> reposInformation = apacheReposWebClient.getApacheReposByHttpClient();
		return reposInformation;
	}

	public List<Response> getTopFiveReposWithMostDownloads() {
		// Hashtable<String, ReleaseInformation> hashtableOfReleaseInfoByRepo = new
		// Hashtable<>();
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		int count = 0;
		ArrayList<Response> responseList = new ArrayList<>();
		// for (RepoInformation repoInformation : getApacheReposList()) {
		// System.out.println(count++);
		// Response response = new Response();
		// ReleaseInformation releaseInformation = apacheReposWebClient
		// .getApacheReposReleaseInfoByHttpClient(repoInformation.getName());
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
				new Response("xyz", 521, "xoxo", 12, "https://api.github.com/repos/apache/tapestry3/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 111, "https://api.github.com/repos/apache/maven-mvnd/contributors"));
		responseList.add(new Response("third", 521, "xoxo", 12232,
				"https://api.github.com/repos/apache/predictionio/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 123, "https://api.github.com/repos/apache/flink-cdc/contributors"));
		responseList.add(new Response("xyz", 521, "xoxo", 112, "https://api.github.com/repos/apache/tvm/contributors"));
		responseList.add(
				new Response("xyz", 521, "xoxo", 1299, "https://api.github.com/repos/apache/streampipes/contributors"));
		responseList.add(new Response("first", 521, "xoxo", 1299999,
				"https://api.github.com/repos/apache/superset/contributors"));
		responseList.add(new Response("second", 521, "xoxo", 1289898,
				"https://api.github.com/repos/apache/camel-k/contributors"));
		responseList.add(new Response("xyz", 521, "xoxo", 13, "https://api.github.com/repos/apache/hop/contributors"));
		responseList
				.add(new Response("xyz", 521, "xoxo", 189, "https://api.github.com/repos/apache/geode/contributors"));
		responseList
				.add(new Response("xyz", 521, "xoxo", 120, "https://api.github.com/repos/apache/xalan-c/contributors"));
		Collections.sort(responseList);
		System.out.println("hashtable -> " + responseList.size());
		System.out.println("Sorted list -> " + responseList.toString());

		return responseList.subList(0, Math.min(responseList.size(), 5));
	}

	public Hashtable<String, List<ContributorInformation>> getTopTenReposContributor() {
		List<Response> topFiveRepos = getTopFiveReposWithMostDownloads();
		Hashtable<String, List<ContributorInformation>> contributorsTableofRepo = new Hashtable<String, List<ContributorInformation>>();
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		for (Response response : topFiveRepos) {
			List<ContributorInformation> contributorInformationList = new ArrayList<ContributorInformation>();
			contributorInformationList = apacheReposWebClient.getReposContributor(response.getContributorUrl());
			Collections.sort(contributorInformationList);
			contributorsTableofRepo.put(response.getRepoName(),
					contributorInformationList.subList(0, Math.min(contributorInformationList.size(), 10)));
		}
		System.out.println("hashtable: " + contributorsTableofRepo);

		return contributorsTableofRepo;
	}

	public void fnSaveContributorInfo() {

		Hashtable<String, List<ContributorInformation>> contributorsTableofRepo = getTopTenReposContributor();
		ApacheReposWebClient apacheReposWebClient = new ApacheReposWebClient();
		for (Entry<String, List<ContributorInformation>> repoTable : contributorsTableofRepo.entrySet()) {
			for (ContributorInformation contributorInformation : repoTable.getValue()) {
				System.out.println("contributorInformation.getLogin() -> " + contributorInformation.getLogin());
				UserInformation userInformation = apacheReposWebClient
						.getUserInformation(contributorInformation.getLogin());
				userInformation.setRepoName(repoTable.getKey());
				userInformation.setCommitCount(contributorInformation.getContributions());
				// userInformation.getLogin();
				// userInformation.getName();
				// userInformation.getLocation();
				// userInformation.getCompany();
				System.out.println("userInformation -> " + userInformation.toString());
				try {
					System.out.println("try to saving...");
					setupDatabase(userInformation);
					System.out.println("saved...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setupDatabase(UserInformation userInformation) {
		System.out.println("calling setupDatabase()....");
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
			saveContributors(connection, userInformation);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void saveContributors(Connection connection, UserInformation userInformation) throws SQLException {

		System.out.println("calling saveContributors()....");

		String contributorInfo = "INSERT INTO contributor_info (repository_name, contributor_username, contributor_name, contributor_location, contributor_company, commit_count) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(contributorInfo)) {
			preparedStatement.setString(1, userInformation.getRepoName());
			preparedStatement.setString(2, userInformation.getLogin());
			preparedStatement.setString(3, userInformation.getName());
			preparedStatement.setString(4, userInformation.getLocation());
			preparedStatement.setString(5, userInformation.getCompany());
			preparedStatement.setLong(6, userInformation.getCommitCount());

			System.out.println("Insertion -> " + preparedStatement.executeUpdate());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Query all.
		String contributorInfoSelect = "SELECT repository_name, contributor_username, contributor_name, contributor_location, contributor_company, commit_count FROM contributor_info";
		try (
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(contributorInfoSelect);) {
			while (rs.next()) {
				// Retrieve by column name
				// int id = rs.getInt("id");
				String title = rs.getString("repository_name");
				String odt = rs.getString("contributor_username");
				long duration = rs.getLong("commit_count");
				System.out.println("id-> " + "" + " - " + title + " - " + odt + " - " + duration);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}