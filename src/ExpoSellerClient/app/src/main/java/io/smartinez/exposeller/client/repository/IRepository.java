package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public interface IRepository<T> {
    /**
     * Method to insert entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    void insert(T entity) throws IOException;

    /**
     * Method to update entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    void update(T entity) throws IOException;

    /**
     * Method to delete entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    void delete(T entity) throws IOException;

    /**
     * Method to search with specific date the entities
     *
     * @param dateEntities The date object
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    List<T> getBySpecificDate(Date dateEntities) throws IOException;

    /**
     * Method to search the entity by docId
     *
     * @param docId The id of entity
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the repository
     */
    T getByDocId(String docId) throws IllegalAccessException, IOException;

    /**
     * Method to search the entity by friendlyId
     *
     * @param friendlyId The friendlyId of Entity
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the repository
     */
    T getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException;
}
