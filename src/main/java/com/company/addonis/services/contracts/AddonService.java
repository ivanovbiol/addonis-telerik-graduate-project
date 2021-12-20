package com.company.addonis.services.contracts;

import com.company.addonis.models.Addon;

import java.util.List;
import java.util.Optional;

public interface AddonService {
    List<Addon> getAll();

    Addon getById(int id);

    void create(Addon addon);

    void delete(int id);

    List<Addon> filter(Optional<String> addonName,
                       Optional<String> ideName,
                       Optional<String> featured,
                       Optional<String> username,
                       Optional<String> sortBy);

    void update(Addon addon);

    List<Addon> getByColumn(String field, int count);

    List<Addon> getAllAddonsByUser(int id);

    List<Addon> getByStatus(String approved);

    long getTotalDownloads();

    List<Addon> getPendingAddonsByName(String name);
}
