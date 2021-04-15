package io.smartinez.exposeller.client.repository.datasource;

import java.io.Closeable;

import io.smartinez.exposeller.client.domain.IModel;

public interface IDataSource extends Closeable {
    void insert(IModel entityObj);

    IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException;
    IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException;

    void update(String docId, IModel entityObj);

    void delete(String docId, Class<? extends IModel> entityClass);
}
