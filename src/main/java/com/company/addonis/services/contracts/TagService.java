package com.company.addonis.services.contracts;

import com.company.addonis.models.Tag;

import java.util.List;

public interface TagService {
    Tag getById(int id);

    List<Tag> getAll();

    Tag getByValue(String value);

    void create(Tag tag);
}
