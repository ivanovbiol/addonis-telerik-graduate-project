package com.company.addonis.models.dtos;

import javax.validation.constraints.*;

public class AddonDto {

    @NotBlank(message = "Name can not be empty.")
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30 symbols.")
    private String name;

    @Positive
    private int ideId;

    @Size(min = 10, message = "Description should be at least 10 characters.")
    private String description;

    @NotBlank(message = "List your tags separated by comma (e.g. Tag1, Tag2, Tag3)")
    private String tags;

    @NotNull(message = "Origin link can not be empty.")
    @Pattern(regexp = "https://github\\.com/.+/.+",
            message = "Origin Link should be in a GitHub repository.")
    private String originLink;

    public AddonDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdeId() {
        return ideId;
    }

    public void setIdeId(int ideId) {
        this.ideId = ideId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOriginLink() {
        return originLink;
    }

    public void setOriginLink(String originLink) {
        this.originLink = originLink;
    }
}
