package org.acme.dto;

public class Response implements Comparable<Response> {

    private long id;
    private String tagName;
    private String repoName;
    private long downlaodCount;
    private String contributorUrl;

    public Response(long id, String tagName, String repoName, long downlaodCount, String contributorUrl) {
        this.id = id;
        this.tagName = tagName;
        this.repoName = repoName;
        this.downlaodCount = downlaodCount;
        this.contributorUrl = contributorUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getDownlaodCount() {
        return downlaodCount;
    }

    public void setDownlaodCount(long downlaodCount) {
        this.downlaodCount = downlaodCount;
    }

    public Response() {
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getContributorUrl() {
        return contributorUrl;
    }

    public void setContributorUrl(String contributorUrl) {
        this.contributorUrl = contributorUrl;
    }

    @Override
    public int compareTo(Response object) {
        return Long.compare(object.downlaodCount, this.downlaodCount);
    }

    @Override
    public String toString() {
        return "Response [id=" + id + ", tagName=" + tagName + ", repoName=" + repoName + ", downlaodCount="
                + downlaodCount + ", contributorUrl=" + contributorUrl + "]";
    }
}