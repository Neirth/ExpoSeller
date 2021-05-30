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

    /**
     * Method to insert entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void insert(Advertisement entity) throws IOException {
        mDataSource.insert(entity);
    }

    /**
     * Method to update entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void update(Advertisement entity) throws IOException {
        mDataSource.update(entity.getDocId(), entity);
    }

    /**
     * Method to delete entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void delete(Advertisement entity) throws IOException {
        mDataSource.delete(entity.getDocId(), Advertisement.class);
    }

    /**
     * Method to search with specific date the entities
     *
     * @param dateAdvertisement The date object
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public List<Advertisement> getBySpecificDate(Date dateAdvertisement) throws IOException {
        return mDataSource.getBySpecificDate(dateAdvertisement, Advertisement.class).stream().map(Advertisement.class::cast).collect(Collectors.toList());
    }

    /**
     * Method to search the entity by docId
     *
     * @param docId The id of entity
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public Advertisement getByDocId(String docId) throws IllegalAccessException, IOException {
        return (Advertisement) mDataSource.getByDocId(docId, Advertisement.class);
    }

    /**
     * Method to search the entity by friendlyId
     *
     * @param friendlyId The friendlyId of Entity
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public Advertisement getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException {
        return (Advertisement) mDataSource.getByFriendlyId(friendlyId, Advertisement.class);
    }

    /**
     * Method to search the entities not before date
     *
     * @param nowDate The object date
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    public List<Advertisement> getNotBeforeDate(Date nowDate) throws IOException {
        return mDataSource.getNotBeforeDate(nowDate, Advertisement.class).stream().map(Advertisement.class::cast).collect(Collectors.toList());
    }
}
