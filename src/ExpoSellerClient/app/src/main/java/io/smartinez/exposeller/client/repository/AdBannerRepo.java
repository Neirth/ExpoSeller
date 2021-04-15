package io.smartinez.exposeller.client.repository;

import dagger.Component;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@Component
public class AdBannerRepo implements IRepository<AdBanner> {
    private IDataSource mDatasource;

    private AdBannerRepo(IDataSource dataSource) {
        this.mDatasource = dataSource;
    }

    @Override
    public void insert(AdBanner entity) {
        mDatasource.insert(entity);
    }

    @Override
    public void update(AdBanner entity) {
        mDatasource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(AdBanner entity) {
        mDatasource.delete(entity.getDocId(), AdBanner.class);
    }

    @Override
    public AdBanner getByDocId(String docId) throws IllegalAccessException {
        return (AdBanner) mDatasource.getByDocId(docId, AdBanner.class);
    }

    @Override
    public AdBanner getByFriendlyId(String friendlyId) throws IllegalAccessException {
        return (AdBanner) mDatasource.getByFriendlyId(friendlyId, AdBanner.class);
    }
}
