package io.smartinez.exposeller.client.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@ActivityScoped
public class ConcertRepo implements IRepository<Concert> {
    private IDataSource mDataSource;

    @Inject
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
    public List<Concert> getBySpecificDate(Date dateConcerts) {
        return Arrays.asList((Concert[]) mDataSource.getBySpecificDate(dateConcerts, Concert.class).toArray());
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
