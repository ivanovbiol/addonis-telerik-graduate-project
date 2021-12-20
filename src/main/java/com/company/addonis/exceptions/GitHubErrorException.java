package com.company.addonis.exceptions;

public class GitHubErrorException extends RuntimeException {

    public GitHubErrorException(String message) {
        super(message);
    }
}

