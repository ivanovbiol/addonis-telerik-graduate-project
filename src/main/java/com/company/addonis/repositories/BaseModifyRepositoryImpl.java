package com.company.addonis.repositories;

import com.company.addonis.repositories.contracts.BaseModifyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseModifyRepositoryImpl<T> extends BaseReadRepositoryImpl<T> implements BaseModifyRepository<T> {

    private final SessionFactory sessionFactory;

    public BaseModifyRepositoryImpl(Class<T> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Object entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Object entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        T entityToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entityToDelete);
            session.getTransaction().commit();
        }
    }
}


