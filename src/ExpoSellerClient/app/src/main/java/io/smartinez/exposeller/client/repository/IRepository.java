package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public interface IRepository<T> {
    void insert(T entity) throws IOException;
    void update(T entity) throws IOException;
    void delete(T entity) throws IOException;

    List<T> getBySpecificDate(Date dateConcerts) throws IOException;

    T getByDocId(String docId) throws IllegalAccessException, IOException;
    T getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException;
}
