/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.repository.datasource;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.neirth.exposeller.client.domain.IModel;

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
