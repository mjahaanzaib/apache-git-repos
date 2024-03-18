package org.acme.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoInformation implements Comparable<RepoInformation> {

    private BigDecimal id;

    private String name;

    @JsonProperty("contributors_url")
    private String contributorsUrl;

    @JsonProperty("stargazers_count")
    private long stargazersCount;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(long stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    @Override
    public int compareTo(RepoInformation repoObject) {
        return Long.compare(repoObject.stargazersCount, this.stargazersCount);
    }

    @Override
    public String toString() {
        return "RepoInformation [id=" + id + ", name=" + name + ", contributorsUrl=" + contributorsUrl
                + ", stargazersCount=" + stargazersCount + "]";
    }
}