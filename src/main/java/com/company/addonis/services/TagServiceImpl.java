package com.company.addonis.services;

import com.company.addonis.models.Tag;
import com.company.addonis.repositories.contracts.TagRepository;
import com.company.addonis.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag getById(int id) {
        return tagRepository.getById(id);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }

    @Override
    public Tag getByValue(String value) {
        return tagRepository.getByValue(value);
    }

    @Override
    public void create(Tag tag) {
        tagRepository.create(tag);
    }
}
