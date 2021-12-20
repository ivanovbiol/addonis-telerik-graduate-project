package com.company.addonis.services;

import com.company.addonis.config.GitHubConfig;
import com.company.addonis.exceptions.GitHubErrorException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.GitHubData;
import com.company.addonis.repositories.contracts.GitHubRepository;
import com.company.addonis.services.contracts.GitHubDataService;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GitHubDataServiceImpl implements GitHubDataService {

    public static final String ORIGIN_LINK_ERROR_MESSAGE = "Unable to connect GitHub." +
            "Please check if your origin link is valid.";
    public static final int GITHUB_DEFAULT_URL_LENGTH = 19;

    private final GitHubRepository gitHubRepository;
    private final GitHubConfig gitHubConfig;

    @Autowired
    public GitHubDataServiceImpl(GitHubRepository gitHubRepository, GitHubConfig gitHubConfig) {
        this.gitHubRepository = gitHubRepository;
        this.gitHubConfig = gitHubConfig;
    }

    @Override
    public GitHubData getDataFromGitHubAndCreate(Addon addon) {
        GitHubData gitHubData = getDataFromGitHub(addon.getOriginLink(), null);
        gitHubRepository.create(gitHubData);
        return gitHubData;
    }

    @Override
    public void getDataFromGitHubAndUpdate(Addon addon) {
        GitHubData gitHubData = getDataFromGitHub(addon.getOriginLink(), addon.getGithubData());
        gitHubRepository.update(gitHubData);
    }

    public GitHubData getDataFromGitHub(String originLink, GitHubData gitHubData) {

        if (gitHubData == null) {
            gitHubData = new GitHubData();
        }

        String gitHubOriginLink = originLink.substring(GITHUB_DEFAULT_URL_LENGTH);
        try {

            GHRepository repository = gitHubConfig.gitHubTokenLink().getRepository(gitHubOriginLink);

            gitHubData.setLastCommitTittle(repository.listCommits().toList().get(0).getCommitShortInfo().getMessage());

            gitHubData.setOpenIssuesCount(repository.getOpenIssueCount());

            gitHubData.setPullRequestsCount(repository.getPullRequests(GHIssueState.OPEN).size());

            gitHubData.setLastCommitDate(repository.listCommits().toList().get(0).getCommitShortInfo().getCommitDate());

            return gitHubData;
        } catch (IOException e) {
            throw new GitHubErrorException(ORIGIN_LINK_ERROR_MESSAGE);
        }
    }
}
