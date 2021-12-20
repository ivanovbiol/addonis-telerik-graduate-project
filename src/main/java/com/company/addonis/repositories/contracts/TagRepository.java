package com.company.addonis.repositories.contracts;

import com.company.addonis.models.Tag;

public interface TagRepository extends BaseModifyRepository<Tag>{
    Tag getByValue(String value);
}
