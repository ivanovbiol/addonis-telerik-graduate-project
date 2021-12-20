package com.company.addonis.repositories;

import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.repositories.contracts.BaseReadRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class BaseReadRepositoryImpl<T> implements BaseReadRepository<T> {

    private final Class<T> clazz;

    private final SessionFactory sessionFactory;

    public BaseReadRepositoryImpl(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    @Override
    public T getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            T object = session.get(clazz, id);
            if (object == null) {
                throw new EntityNotFoundException(clazz.getSimpleName(), id);
            }
            return object;
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery("from " + clazz.getSimpleName(), clazz);
            return query.list();
        }
    }

    @Override
    public <V> T getByField(String name, V value) {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery(String.format("from %s where %s = :%s", clazz.getSimpleName(), name, name), clazz);
            query.setParameter(name, value);

            List<T> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException(clazz.getSimpleName(), name, value.toString());
            }

            return result.get(0);
        }
    }

}
