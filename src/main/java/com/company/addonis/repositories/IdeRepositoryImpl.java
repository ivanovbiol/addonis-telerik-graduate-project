package com.company.addonis.repositories;

import com.company.addonis.models.IDE;
import com.company.addonis.repositories.contracts.IdeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class IdeRepositoryImpl extends BaseModifyRepositoryImpl<IDE> implements IdeRepository {

    public IdeRepositoryImpl(SessionFactory sessionFactory) {
        super(IDE.class, sessionFactory);
    }
}
