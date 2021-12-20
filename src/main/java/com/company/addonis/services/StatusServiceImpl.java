package com.company.addonis.services;

import com.company.addonis.models.Status;
import com.company.addonis.repositories.contracts.StatusRepository;
import com.company.addonis.services.contracts.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status getById(int id) {
        return statusRepository.getById(id);
    }
}
