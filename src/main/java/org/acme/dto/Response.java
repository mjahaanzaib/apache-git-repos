package org.acme.dto;

public class Response implements Comparable<Response> {
    private String repoName;
    private long id;
    private String tagName;
    private long downlaodCount;
    private String contributorUrl;

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

    public Response(String repoName, long id, String tagName, long downlaodCount,String contributorUrl) {
        this.repoName = repoName;
        this.id = id;
        this.tagName = tagName;
        this.downlaodCount = downlaodCount;
        this.contributorUrl = contributorUrl;
    }

    public String getContributorUrl() {
        return contributorUrl;
    }

    public void setContributorUrl(String contributorUrl) {
        this.contributorUrl = contributorUrl;
    }

    @Override
    public int compareTo(Response object) {
        return Long.compare(object.downlaodCount,this.downlaodCount);
    }

    @Override
    public String toString() {
        return "Response [repoName=" + repoName + ", id=" + id + ", tagName=" + tagName + ", downlaodCount="
                + downlaodCount + "]";
    }

    
}
