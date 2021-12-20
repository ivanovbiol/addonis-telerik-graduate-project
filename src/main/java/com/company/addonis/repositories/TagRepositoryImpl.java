package com.company.addonis.repositories;

import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Tag;
import com.company.addonis.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl extends BaseModifyRepositoryImpl<Tag> implements TagRepository {

    private final static String NO_TAGS_WITH_NAME_TEMPLATE = "No Tags with name %s";

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        super(Tag.class, sessionFactory);
    }

    @Override
    public Tag getByValue(String value) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Tag> query = session.createQuery(" from Tag where name like :valueParam ", Tag.class);
            query.setParameter("valueParam", value);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException(String.format(NO_TAGS_WITH_NAME_TEMPLATE, value));
            }
            return query.list().get(0);
        }
    }
}
