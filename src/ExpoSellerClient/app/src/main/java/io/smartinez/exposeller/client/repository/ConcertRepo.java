package io.smartinez.exposeller.client.repository;

import dagger.Component;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@Component
public class ConcertRepo implements IRepository<Concert> {
    private IDataSource mDataSource;

    public ConcertRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public void insert(Concert entity) {
        mDataSource.insert(entity);
    }

    @Override
    public void update(Concert entity) {
        mDataSource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(Concert entity) {
        mDataSource.delete(entity.getDocId(), Concert.class);
    }

    @Override
    public Concert getByDocId(String docId) throws IllegalAccessException {
        return (Concert) mDataSource.getByDocId(docId, Concert.class);
    }

    @Override
    public Concert getByFriendlyId(String friendlyId) throws IllegalAccessException {
        return (Concert) mDataSource.getByFriendlyId(friendlyId, Concert.class);
    }
}
