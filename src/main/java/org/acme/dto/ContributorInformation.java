package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContributorInformation implements Comparable<ContributorInformation> {

    private String login;

    private long contributions;

    public long getContributions() {
        return contributions;
    }

    public void setContributions(long contributions) {
        this.contributions = contributions;
    }

    @Override
    public int compareTo(ContributorInformation o) {
        return Long.compare(o.contributions,this.contributions);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {

        if(login.contains("["))
            this.login = login.split("\\[[^\\[]*\\]")[0];
        else
            this.login = login;
    }

    @Override
    public String toString() {
        return "ContributorInformation [login=" + login + ", contributions=" + contributions + "]";
    }

    
}