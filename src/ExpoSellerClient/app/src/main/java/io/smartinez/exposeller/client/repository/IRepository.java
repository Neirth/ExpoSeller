/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
