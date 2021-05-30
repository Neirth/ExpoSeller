package io.smartinez.exposeller.client.repository.datasource;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.smartinez.exposeller.client.domain.IModel;

public interface IDataSource{
    /**
     * Method to insert a entity in the database
     *
     * @param entityObj The entity object
     * @throws IOException In case of not being able to access the database
     */
    void insert(IModel entityObj) throws IOException;

    /**
     * Method to update a entity in the database
     *
     * @param docId The entity id
     * @param entityObj The entity object
     * @throws IOException In case of not being able to access the database
     */
    void update(String docId, IModel entityObj) throws IOException;

    /**
     * Method to delete a entity in the database
     *
     * @param docId The entity id
     * @param entityClass The entity class
     * @throws IOException In case of not being able to access the database
     */
    void delete(String docId, Class<? extends IModel> entityClass) throws IOException;

    /**
     * Method to obtain the records from a date passed by parameter
     *
     * @param date The date from where to start
     * @param entityClass The entity class
     * @return The list of entities that have been returned from the list
     * @throws IOException In case of not being able to access the database
     */
    List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) throws IOException;

    /**
     * Method to obtain a list of entities of a specific date
     *
     * @param date The date to search
     * @param entityClass The entity class
     * @return The list of entities that have been returned from the list
     * @throws IOException In case of not being able to access the database
     */
    List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) throws IOException;

    /**
     * Method to obtain an entity from its id
     *
     * @param docId The entity id
     * @param entityClass The entity class
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the database
     */
    IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException;

    /**
     * Method to obtain an entity from its friendlyId
     *
     * @param friendlyId The entity friendlyId
     * @param entityClass The entity class
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the database
     */
    IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException;

    /**
     * Method to access the system administrator mode
     *
     * @param email The user administrator email
     * @param password The user administrator password
     * @throws IOException In case of not being able to access the database
     */
    void loginDatabase(String email, String password) throws IOException;

    /**
     * Method to be able to close the system administrator mode
     *
     * @throws IOException In case of not being able to access the database
     */
    void logoutDatabase() throws IOException;
}
