package com.company.addonis.repositories;

import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Addon;
import com.company.addonis.repositories.contracts.AddonRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class AddonRepositoryImpl extends BaseModifyRepositoryImpl<Addon> implements AddonRepository {

    public AddonRepositoryImpl(SessionFactory sessionFactory) {
        super(Addon.class, sessionFactory);
    }

    @Override
    public List<Addon> filter(Optional<String> addonName,
                              Optional<String> ideName,
                              Optional<String> featured,
                              Optional<String> username,
                              Optional<String> sortBy) {
        try (Session session = getSessionFactory().openSession()) {
            StringBuilder stringBuilder =
                    new StringBuilder(" from Addon where status.name like :statusParam ");
            ArrayList<String> filtersList = new ArrayList<>();
            HashMap<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("statusParam", "Approved");

            if (!addonName.get().equals("-1")) {
                addonName.ifPresent(value -> {
                    filtersList.add("name like :addonNameParam");
                    parametersMap.put("addonNameParam", value);
                });
            }

            if (!ideName.get().equals("-1")) {
                ideName.ifPresent(value -> {
                    filtersList.add("ide.name like :ideNameParam");
                    parametersMap.put("ideNameParam", value);
                });
            }

            if (!featured.get().equals("-1")) {
                featured.ifPresent(value -> {
                    filtersList.add("featured like :featuredParam");
                    parametersMap.put("featuredParam", !value.equals("0"));
                });
            }

            if (!username.get().equals("-1")) {
                username.ifPresent(value -> {
                    filtersList.add("creator.username like :usernameParam");
                    parametersMap.put("usernameParam", value);
                });
            }

            if (!filtersList.isEmpty()) {
                stringBuilder.append(" and ").append(String.join(" and ", filtersList));
            }

            if (!sortBy.get().equals("-1")) {
                if (sortBy.isPresent()) {
                    addSortingCriteria(sortBy, stringBuilder);
                }
            }

            Query<Addon> query = session.createQuery(stringBuilder.toString(), Addon.class);
            query.setProperties(parametersMap);

            return query.list();
        }
    }

    private void addSortingCriteria(Optional<String> sortBy, StringBuilder stringBuilder) {
        stringBuilder.append(" order by ");
        String[] sortingParameters = sortBy.get().split(", ");

        switch (sortingParameters[0]) {
            case "name":
                stringBuilder.append(" name ");
                break;
            case "number of downloads":
                stringBuilder.append(" downloadCount ");
                break;
            case "upload date":
                stringBuilder.append(" uploadDate ");
                break;
            case "last commit date":
                stringBuilder.append(" githubData.lastCommitDate ");
                break;
        }

        if (sortingParameters.length > 1 && sortingParameters[1].equals("desc")) {
            stringBuilder.append("desc");
        }
    }

    @Override
    public List<Addon> getByColumn(String column, int count) {
        try (Session session = getSessionFactory().openSession()) {
            String sqlQuery = " from Addon where status.id = 2 order by " + column + "  desc ";
            Query<Addon> query = session.createQuery(sqlQuery, Addon.class).setMaxResults(count);
            return query.list();
        }
    }

    @Override
    public List<Addon> getAddonsByUser(int id) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Addon> query = session.createQuery(
                    "from Addon where creator.id = :id", Addon.class);
            query.setParameter("id", id);

            return query.list();
        }
    }

    @Override
    public List<Addon> getByStatus(String status) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Addon> query = session.createQuery(
                    " from Addon where status.name like :statusParam ");
            query.setParameter("statusParam", status);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Addons", "status", status);
            }
            return query.list();

        }
    }

    @Override
    public long getTotalDownloadCount() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("SELECT SUM(addon.downloadCount) FROM Addon addon",
                    Long.class).getSingleResult();
        }
    }

    @Override
    public List<Addon> getPendingAddonsByName(String name) {
        try (Session session = getSessionFactory().openSession()) {
            String pendingString = "Pending";
            Query<Addon> query = session.createQuery(
                    " from Addon where name like :nameParam and status.name like :statusParam");
            query.setParameter("nameParam", name);
            query.setParameter("statusParam", pendingString);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Addons", "name", name);
            }
            return query.list();
        }
    }
}

