package com.company.addonis.services;

import com.company.addonis.models.Addon;
import com.company.addonis.models.GitHubData;
import com.company.addonis.repositories.contracts.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.addonis.Helpers.createMockAddon;
import static com.company.addonis.Helpers.createMockGitHubData;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

    @Mock
    private GitHubRepository mockRepository;

    @InjectMocks
    @Spy
    private GitHubDataServiceImpl gitHubDataService;

    @Test
    public void create_should_CallRepository() {
        Addon mockAddon = createMockAddon();
        GitHubData gitHubData = createMockGitHubData();
        doReturn(gitHubData).when(gitHubDataService)
                .getDataFromGitHub(eq(mockAddon.getOriginLink()), isNull());

        gitHubDataService.getDataFromGitHubAndCreate(mockAddon);

        Mockito.verify(mockRepository).create(gitHubData);
    }

    @Test
    public void update_should_CallRepository() {

        Addon mockAddon = createMockAddon();
        doReturn(mockAddon.getGithubData()).when(gitHubDataService)
                .getDataFromGitHub(eq(mockAddon.getOriginLink()), eq(mockAddon.getGithubData()));

        gitHubDataService.getDataFromGitHubAndUpdate(mockAddon);

        Mockito.verify(mockRepository).update(mockAddon.getGithubData());
    }

}
