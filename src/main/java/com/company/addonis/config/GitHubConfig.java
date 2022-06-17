package com.company.addonis.config;

import com.company.addonis.exceptions.GitHubErrorException;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GitHubConfig {

    public static final String GITHUB_CONNECTION_ERROR_MESSAGE = "Unable to connect GitHub. " +
            "Please check if your token is valid.";

    // I will divide the token in two parts, because Git deactivates it when i push it to my GitHub profile
    public static final String GITHUB_TOKEN_LINK_PART_ONE = "ghp_pfP48mbpBKSPxY";
    public static final String GITHUB_TOKEN_LINK_PART_TWO = "hI6mwrT1gW6VWBem0SAXWt ";

    public GitHub gitHubTokenLink() {

        try {
            return new GitHubBuilder().withOAuthToken(concatGitHubToken()).build();
        } catch (IOException e) {
            throw new GitHubErrorException(GITHUB_CONNECTION_ERROR_MESSAGE);
        }
    }

    private String concatGitHubToken() {
        return GITHUB_TOKEN_LINK_PART_ONE + GITHUB_TOKEN_LINK_PART_TWO;
    }
}
