package com.company.addonis.models.mappers;

import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.Tag;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.AddonDto;
import com.company.addonis.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AddonMapper {

    private final AddonService addonService;
    private final IdeService ideService;
    private final TagService tagService;
    private final StatusService statusService;
    private final GitHubDataService gitHubDataService;

    @Autowired
    public AddonMapper(AddonService addonService,
                       IdeService ideService,
                       TagService tagService,
                       StatusService statusService,
                       GitHubDataService gitHubDataService) {
        this.addonService = addonService;
        this.ideService = ideService;
        this.tagService = tagService;
        this.statusService = statusService;
        this.gitHubDataService = gitHubDataService;
    }

    public Addon fromDto(AddonDto addonDto, User loggedUser) throws IOException {
        Addon addon = new Addon();
        dtoToObject(addonDto, addon);
        addon.setCreator(loggedUser);
        return addon;
    }

    public Addon fromDto(AddonDto addonDto, int id) throws IOException {
        Addon addon = addonService.getById(id);
        dtoToObject(addonDto, addon);
        return addon;
    }

    private void dtoToObject(AddonDto addonDto, Addon addon) throws IOException {
        addon.setName(addonDto.getName());
        addon.setIde(ideService.getById(addonDto.getIdeId()));
        addon.setDescription(addonDto.getDescription());
        if (addonDto.getTags() != null || !addonDto.getTags().equals("")) {
            addon.setTags(getTags(addonDto.getTags()));
        } else {
            throw new UnauthorizedOperationException("Tags can not be empty!");
        }
        addon.setOriginLink(addonDto.getOriginLink());
        addon.setStatus(statusService.getById(1));
        addon.setUploadDate(LocalDate.now());
        //addon.setGithubData(gitHubDataService.getDataFromGitHubAndCreate(addon));
    }

    public AddonDto toDto(Addon addon) {
        AddonDto addonDto = new AddonDto();
        addonDto.setName(addon.getName());
        addonDto.setIdeId(addon.getIde().getId());
        addonDto.setDescription(addon.getDescription());
        addonDto.setTags(addon.getTags().stream().map(Tag::getName).collect(Collectors.joining(", ")));
        addonDto.setOriginLink(addon.getOriginLink());
        return addonDto;
    }

    private Set<Tag> getTags(String tags) {
        List<String> tagsAsList =
                Arrays
                        .stream(tags.trim().split("\\s*,\\s*"))
                        .collect(Collectors.toList());

        for (String currentTag : tagsAsList) {
            try {
                tagService.getByValue(currentTag);
            } catch (EntityNotFoundException e) {
                Tag tag = new Tag();
                tag.setName(currentTag);
                tagService.create(tag);
            }
        }

        return tagsAsList
                .stream()
                .map(tagService::getByValue)
                .collect(Collectors.toSet());
    }

    @Scheduled(cron = "* 00 23 * * *")
    private void updateGitHubData() {
        System.out.println("update");
        List<Addon> addons = addonService.getAll();
        for (Addon addon : addons) {
            gitHubDataService.getDataFromGitHubAndUpdate(addon);
            addonService.update(addon);
        }
    }
}
