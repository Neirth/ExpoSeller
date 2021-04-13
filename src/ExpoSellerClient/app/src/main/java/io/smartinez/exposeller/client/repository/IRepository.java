package io.smartinez.exposeller.client.repository;

public interface IRepository<T> {
    void insert(T entity);
    void update(T entity);
    void delete(T entity);

    T getByDocId(String docId) throws IllegalAccessException;
    T getByFriendlyId(String friendlyId) throws IllegalAccessException;
}
