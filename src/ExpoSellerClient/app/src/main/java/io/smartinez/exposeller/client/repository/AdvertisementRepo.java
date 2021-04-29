package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@Singleton
public class AdvertisementRepo implements IRepository<Advertisement> {
    private final IDataSource mDataSource;

    @Inject
    public AdvertisementRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public void insert(Advertisement entity) throws IOException {
        mDataSource.insert(entity);
    }

    @Override
    public void update(Advertisement entity) throws IOException {
        mDataSource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(Advertisement entity) throws IOException {
        mDataSource.delete(entity.getDocId(), Advertisement.class);
    }

    @Override
    public List<Advertisement> getBySpecificDate(Date dateConcerts) throws IOException {
        return mDataSource.getBySpecificDate(dateConcerts, Advertisement.class).stream().map(Advertisement.class::cast).collect(Collectors.toList());
    }

    @Override
    public Advertisement getByDocId(String docId) throws IllegalAccessException, IOException {
        return (Advertisement) mDataSource.getByDocId(docId, Advertisement.class);
    }

    @Override
    public Advertisement getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException {
        return (Advertisement) mDataSource.getByFriendlyId(friendlyId, Advertisement.class);
    }

    public List<Advertisement> getNotBeforeDate(Date nowDate) throws IOException {
        return mDataSource.getNotBeforeDate(nowDate, Advertisement.class).stream().map(Advertisement.class::cast).collect(Collectors.toList());
    }
}
