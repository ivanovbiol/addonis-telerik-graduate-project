package com.company.addonis.services.contracts;

import com.company.addonis.models.IDE;

import java.util.List;

public interface IdeService {
    IDE getById(int id);

    List<IDE> getAll();
}
