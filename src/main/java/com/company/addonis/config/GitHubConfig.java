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
    public static final String GITHUB_TOKEN_LINK = "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";

    public GitHub gitHubTokenLink() {

        try {
            return new GitHubBuilder().withOAuthToken(GITHUB_TOKEN_LINK).build();
        } catch (IOException e) {
            throw new GitHubErrorException(GITHUB_CONNECTION_ERROR_MESSAGE);
        }
    }

}
