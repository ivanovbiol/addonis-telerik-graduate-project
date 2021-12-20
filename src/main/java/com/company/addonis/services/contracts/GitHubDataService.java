package com.company.addonis.services.contracts;

import com.company.addonis.models.Addon;
import com.company.addonis.models.GitHubData;

public interface GitHubDataService {

    GitHubData getDataFromGitHubAndCreate(Addon addon);

    void getDataFromGitHubAndUpdate(Addon addon);
}
