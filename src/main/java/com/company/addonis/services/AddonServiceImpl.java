package com.company.addonis.services;

import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Addon;
import com.company.addonis.repositories.contracts.AddonRepository;
import com.company.addonis.services.contracts.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.company.addonis.utils.ObjectFieldsSetter.*;

@Service
public class AddonServiceImpl implements AddonService {

    private final AddonRepository addonRepository;

    @Autowired
    public AddonServiceImpl(AddonRepository addonRepository) {
        this.addonRepository = addonRepository;
    }

    @Override
    public List<Addon> getAll() {
        return addonRepository.getAll();
    }

    @Override
    public Addon getById(int id) {
        Addon addon = addonRepository.getById(id);
        setAddonImageString(addon);
        setAddonTotalScore(addon);
        setAddonCreatorImageString(addon);
        return addon;
    }

    @Override
    public void create(Addon addon) {
        validateAddonName(addon);
        addonRepository.create(addon);
    }

    @Override
    public void update(Addon addon) {
        validateAddonName(addon);
        addonRepository.update(addon);
    }

    @Override
    public void delete(int id) {
        addonRepository.delete(id);
    }

    @Override
    public List<Addon> filter(Optional<String> addonName,
                              Optional<String> ideName,
                              Optional<String> featured,
                              Optional<String> username,
                              Optional<String> sortBy) {
        List<Addon> addons = addonRepository.filter(addonName, ideName, featured, username, sortBy);
        setAddonImageString(addons);
        setAddonCreatorImageString(addons);
        setAddonTotalScore(addons);
        return addons;
    }

    @Override
    public List<Addon> getByColumn(String field, int count) {
        List<Addon> addons = addonRepository.getByColumn(field, count);
        setAddonImageString(addons);
        setAddonTotalScore(addons);
        setAddonCreatorImageString(addons);
        return addons;
    }

    @Override
    public List<Addon> getAllAddonsByUser(int id) {
        List<Addon> addons = addonRepository.getAddonsByUser(id);
        setAddonImageString(addons);
        setAddonCreatorImageString(addons);
        return addons;
    }

    @Override
    public List<Addon> getByStatus(String status) {
        List<Addon> addons = addonRepository.getByStatus(status);
        setAddonImageString(addons);
        return addons;
    }

    private void validateAddonName(Addon addon) {
        boolean duplicateExists = true;
        try {
            Addon addonExist = addonRepository.getByField("name", addon.getName());
            if (addonExist.getId() == addon.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Addon", "name", addon.getName());
        }
    }

    @Override
    public long getTotalDownloads() {
        return addonRepository.getTotalDownloadCount();
    }

    @Override
    public List<Addon> getPendingAddonsByName(String name) {
        List<Addon> addons = addonRepository.getPendingAddonsByName(name);
        setAddonImageString(addons);
        return addons;
    }
}
