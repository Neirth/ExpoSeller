package io.smartinez.exposeller.client.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@ViewModelScoped
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
        return mDataSource.getBySpecificDate(dateConcerts, Concert.class).stream().map(Concert.class::cast).collect(Collectors.toList());
    }

    @Override
    public Concert getByDocId(String docId) throws IllegalAccessException {
        return (Concert) mDataSource.getByDocId(docId, Concert.class);
    }

    @Override
    public Concert getByFriendlyId(String friendlyId) throws IllegalAccessException {
        return (Concert) mDataSource.getByFriendlyId(friendlyId, Concert.class);
    }

    public List<Concert> getNotBeforeDate(Date nowDate) {
        return mDataSource.getNotBeforeDate(nowDate, Concert.class).stream().map(Concert.class::cast).collect(Collectors.toList());
    }
}
