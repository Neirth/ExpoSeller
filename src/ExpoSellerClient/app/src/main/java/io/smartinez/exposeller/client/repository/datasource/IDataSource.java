package io.smartinez.exposeller.client.repository.datasource;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.smartinez.exposeller.client.domain.IModel;

public interface IDataSource extends Closeable {
    void insert(IModel entityObj) throws IOException;

    List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) throws IOException;
    List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) throws IOException;

    IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException;
    IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException;

    void update(String docId, IModel entityObj) throws IOException;

    void delete(String docId, Class<? extends IModel> entityClass) throws IOException;
}
