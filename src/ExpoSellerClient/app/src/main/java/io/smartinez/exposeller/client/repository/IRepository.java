package io.smartinez.exposeller.client.repository;

import java.util.Date;
import java.util.List;


public interface IRepository<T> {
    void insert(T entity);
    void update(T entity);
    void delete(T entity);

    List<T> getBySpecificDate(Date dateConcerts);

    T getByDocId(String docId) throws IllegalAccessException;
    T getByFriendlyId(String friendlyId) throws IllegalAccessException;
}
