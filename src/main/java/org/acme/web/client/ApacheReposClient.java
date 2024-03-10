package org.acme.web.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "apache-repos-api")
public interface ApacheReposClient {

    @GET
    @Path(value = "/apache/repos")
    public Response getApacheRepos(@QueryParam(value = "page") int page, @QueryParam(value = "per_page") int perPage);
}