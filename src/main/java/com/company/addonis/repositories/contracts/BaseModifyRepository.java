package com.company.addonis.repositories.contracts;

public interface BaseModifyRepository<T> extends BaseReadRepository<T> {

    void create(T entity);

    void update(T entity);

    void delete(int id);

}
