package org.acme.dto;

public class ReposInformation {

    RepoInformation repoInformation;

    public RepoInformation getRepoInformation() {
        return repoInformation;
    }

    public void setRepoInformation(RepoInformation repoInformation) {
        this.repoInformation = repoInformation;
    }

    @Override
    public String toString() {
        return "ReposInformation [repoInformation=" + repoInformation + "]";
    }
    
}