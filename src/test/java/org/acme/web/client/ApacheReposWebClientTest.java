package org.acme.web.client;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ApacheReposWebClientTest {

    @Test
    void testFnGetApacheReposByHttpClient() {
        given().when()
                .get("https://api.github.com/orgs/apache/repos").then()
                .assertThat()
                .body("size()", is(30))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void testFnGetApacheReposReleaseInfoByHttpClient() {
        given().when()
                .get("https://api.github.com/repos/apache/superset/releases/latest").then()
                .assertThat()
                .body("author.login", equalTo("github-actions[bot]"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void testFnGetReposContributor() {
        given().when()
                .get("https://api.github.com/repos/apache/superset/contributors").then()
                .assertThat()
                .body("size()", is(30))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void testFnGetUserInformation() {
        given().when()
                .get("https://api.github.com/users/dependabot").then()
                .assertThat()
                .body("login", equalTo("dependabot"))
                .body("id", equalTo(27347476))
                .body("name", equalTo("Dependabot"))
                .body("email", equalTo("support@github.com"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void testApiRateLimit() {
        given().when()
                .get("https://api.github.com/orgs/apache/repos").then()
                .assertThat()
                .body(containsString("API rate limit exceeded for"))
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void testTokenAuthentication() {
        given().when()
                .get("https://api.github.com/orgs/apache/repos")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}