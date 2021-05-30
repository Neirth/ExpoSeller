package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@Singleton
public class ConcertRepo implements IRepository<Concert> {
    private final IDataSource mDataSource;

    @Inject
    public ConcertRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    /**
     * Method to insert entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void insert(Concert entity) throws IOException {
        mDataSource.insert(entity);
    }

    /**
     * Method to update entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void update(Concert entity) throws IOException {
        mDataSource.update(entity.getDocId(), entity);
    }

    /**
     * Method to delete entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void delete(Concert entity) throws IOException {
        mDataSource.delete(entity.getDocId(), Concert.class);
    }

    /**
     * Method to search with specific date the entities
     *
     * @param dateConcerts The date object
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public List<Concert> getBySpecificDate(Date dateConcerts) throws IOException {
        return mDataSource.getBySpecificDate(dateConcerts, Concert.class).stream().map(Concert.class::cast).collect(Collectors.toList());
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
    public Concert getByDocId(String docId) throws IllegalAccessException, IOException {
        return (Concert) mDataSource.getByDocId(docId, Concert.class);
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
    public Concert getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException {
        return (Concert) mDataSource.getByFriendlyId(friendlyId, Concert.class);
    }

    /**
     * Method to search the entities not before date
     *
     * @param nowDate The object date
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    public List<Concert> getNotBeforeDate(Date nowDate) throws IOException {
        return mDataSource.getNotBeforeDate(nowDate, Concert.class).stream().map(Concert.class::cast).collect(Collectors.toList());
    }
}
