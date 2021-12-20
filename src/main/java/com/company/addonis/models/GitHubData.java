package com.company.addonis.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "github_data")

public class GitHubData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "open_issues_count")
    private int openIssuesCount;

    @Column(name = "pulls_count")
    private int pullRequestsCount;

    @Column(name = "last_commit_date")
    private Date lastCommitDate;

    @Column(name = "last_commit_tittle")
    private String lastCommitTittle;


    public GitHubData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public int getPullRequestsCount() {
        return pullRequestsCount;
    }

    public void setPullRequestsCount(int pullRequestsCount) {
        this.pullRequestsCount = pullRequestsCount;
    }

    public Date getLastCommitDate() {
        return lastCommitDate;
    }

    public void setLastCommitDate(Date lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public String getLastCommitTittle() {
        return lastCommitTittle;
    }

    public void setLastCommitTittle(String lastCommitTittle) {
        this.lastCommitTittle = lastCommitTittle;
    }
}

