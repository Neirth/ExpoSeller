package io.smartinez.exposeller.client.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@ViewModelScoped
public class AdBannerRepo implements IRepository<AdBanner> {
    private IDataSource mDataSource;

    @Inject
    public AdBannerRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public void insert(AdBanner entity) {
        mDataSource.insert(entity);
    }

    @Override
    public void update(AdBanner entity) {
        mDataSource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(AdBanner entity) {
        mDataSource.delete(entity.getDocId(), AdBanner.class);
    }

    @Override
    public List<AdBanner> getBySpecificDate(Date dateConcerts) {
        return Arrays.asList((AdBanner[]) mDataSource.getBySpecificDate(dateConcerts, AdBanner.class).toArray());
    }

    @Override
    public AdBanner getByDocId(String docId) throws IllegalAccessException {
        return (AdBanner) mDataSource.getByDocId(docId, AdBanner.class);
    }

    @Override
    public AdBanner getByFriendlyId(String friendlyId) throws IllegalAccessException {
        return (AdBanner) mDataSource.getByFriendlyId(friendlyId, AdBanner.class);
    }

    public List<AdBanner> getNotBeforeDate(Date nowDate) {
        return Arrays.asList((AdBanner[]) mDataSource.getNotBeforeDate(nowDate, AdBanner.class).toArray());
    }
}
