package com.company.addonis;

import com.company.addonis.models.*;
import com.company.addonis.models.dtos.ContactUsDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static com.company.addonis.services.EmailSenderServiceImpl.SENDER_MAIL;

public class Helpers {

    public static IDE createMockIDE() {
        IDE mockIde = new IDE();
        mockIde.setId(1);
        mockIde.setName("IDE");
        return mockIde;
    }

    public static Status createMockStatus() {
        Status mockStatus = new Status();
        mockStatus.setId(1);
        mockStatus.setName("Status");
        return mockStatus;
    }

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("username");
        mockUser.setPassword("password");
        mockUser.setEmail(SENDER_MAIL);
        mockUser.setPhoneNumber("0888888888");
        mockUser.setPhoto("Sample String".getBytes());
        mockUser.setEnabled(true);
        mockUser.setAdmin(true);
        mockUser.setUploadedAddons(1);
        return mockUser;
    }

    public static GitHubData createMockGitHubData() {
        GitHubData mockGitHubData = new GitHubData();
        mockGitHubData.setId(1);
        mockGitHubData.setOpenIssuesCount(1);
        mockGitHubData.setPullRequestsCount(1);
        mockGitHubData.setLastCommitDate(new Date());
        mockGitHubData.setLastCommitTittle("Title");
        return mockGitHubData;
    }

    public static Addon createMockAddon() {
        Addon mockAddon = new Addon();
        mockAddon.setId(1);
        mockAddon.setName("Addon");
        mockAddon.setIde(createMockIDE());
        mockAddon.setCreator(createMockUser());
        mockAddon.setDescription("Description");
        mockAddon.setTags(Set.of(createMockTag()));
        mockAddon.setImage("Sample String".getBytes());
        mockAddon.setBinaryContent("Sample String".getBytes());
        mockAddon.setOriginLink("https://github.com/hristian100/Addon1");
        mockAddon.setGithubData(createMockGitHubData());
        mockAddon.setStatus(createMockStatus());
        mockAddon.setDownloadCount(1);
        mockAddon.setTotalScore(1);
        mockAddon.setTotalVoters(1);
        mockAddon.setUploadDate(LocalDate.now());
        return mockAddon;
    }

    public static Tag createMockTag() {
        Tag mockTag = new Tag();
        mockTag.setId(1);
        mockTag.setName("Tag");
        return mockTag;
    }

    public static ConfirmationToken createMockConfirmationToken() {
        ConfirmationToken mockToken = new ConfirmationToken();
        mockToken.setId(1);
        mockToken.setToken("Token");
        mockToken.setUser(createMockUser());
        mockToken.setExpirationDate(LocalDate.now().plusDays(1));
        return mockToken;
    }

    public static ContactUsDto createMockContactUsToken() {
        ContactUsDto mockContactUsDto = new ContactUsDto();
        mockContactUsDto.setYourName("Name");
        mockContactUsDto.setSubject("Subject");
        mockContactUsDto.setYourEmailAddress("mail@mail.com");
        mockContactUsDto.setText("Very long text");
        return mockContactUsDto;
    }
}
