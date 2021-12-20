package com.company.addonis.repositories;

import com.company.addonis.models.ConfirmationToken;
import com.company.addonis.repositories.contracts.ConfirmationTokenRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmationTokenRepositoryImpl extends BaseModifyRepositoryImpl<ConfirmationToken>
        implements ConfirmationTokenRepository {

    public ConfirmationTokenRepositoryImpl(SessionFactory sessionFactory) {
        super(ConfirmationToken.class, sessionFactory);
    }
}
