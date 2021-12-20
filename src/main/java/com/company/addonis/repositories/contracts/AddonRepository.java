package com.company.addonis.repositories.contracts;

import com.company.addonis.models.Addon;

import java.util.List;
import java.util.Optional;

public interface AddonRepository extends BaseModifyRepository<Addon> {

    List<Addon> filter(Optional<String> addonName,
                       Optional<String> ideName,
                       Optional<String> featured,
                       Optional<String> username,
                       Optional<String> sortBy);

    List<Addon> getByColumn(String column, int count);

    List<Addon> getAddonsByUser(int id);

    List<Addon> getByStatus(String status);

    long getTotalDownloadCount();

    List<Addon> getPendingAddonsByName(String name);
}
