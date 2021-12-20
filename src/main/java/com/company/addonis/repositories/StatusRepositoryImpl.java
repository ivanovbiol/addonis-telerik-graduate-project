package com.company.addonis.repositories;

import com.company.addonis.models.Status;
import com.company.addonis.repositories.contracts.StatusRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class StatusRepositoryImpl extends BaseModifyRepositoryImpl<Status> implements StatusRepository {
    public StatusRepositoryImpl(SessionFactory sessionFactory) {
        super(Status.class, sessionFactory);
    }
}
