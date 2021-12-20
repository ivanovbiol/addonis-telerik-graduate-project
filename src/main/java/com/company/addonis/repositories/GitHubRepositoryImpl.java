package com.company.addonis.repositories;

import com.company.addonis.models.GitHubData;
import com.company.addonis.repositories.contracts.GitHubRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GitHubRepositoryImpl extends BaseModifyRepositoryImpl<GitHubData> implements GitHubRepository {

    public GitHubRepositoryImpl(SessionFactory sessionFactory) {
        super(GitHubData.class, sessionFactory);
    }
}
