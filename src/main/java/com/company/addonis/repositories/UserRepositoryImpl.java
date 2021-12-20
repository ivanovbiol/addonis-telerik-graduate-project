package com.company.addonis.repositories;

import com.company.addonis.models.User;
import com.company.addonis.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseModifyRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public List<User> search(String searchedValue) {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where (username" +
                    " like :username and isAdmin=false) or (phoneNumber " +
                    "like :phoneNumber and isAdmin=false) or (email like :email and isAdmin=false)", User.class);
            query.setParameter("username", "%" + searchedValue + "%");
            query.setParameter("phoneNumber", "%" + searchedValue + "%");
            query.setParameter("email", "%" + searchedValue + "%");
            return query.list();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User where isAdmin=false", User.class);
            return query.list();
        }

    }
}

