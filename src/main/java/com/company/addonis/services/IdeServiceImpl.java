package com.company.addonis.services;

import com.company.addonis.models.IDE;
import com.company.addonis.repositories.contracts.IdeRepository;
import com.company.addonis.services.contracts.IdeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeServiceImpl implements IdeService {

    private final IdeRepository ideRepository;

    @Autowired
    public IdeServiceImpl(IdeRepository ideRepository) {
        this.ideRepository = ideRepository;
    }

    @Override
    public IDE getById(int id) {
        return ideRepository.getById(id);
    }

    @Override
    public List<IDE> getAll() {
        return ideRepository.getAll();
    }
}
