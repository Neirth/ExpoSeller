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
package io.neirth.exposeller.client.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.neirth.exposeller.client.domain.Ticket;
import io.neirth.exposeller.client.repository.datasource.IDataSource;

@Singleton
public class TicketRepo implements IRepository<Ticket> {
    private final IDataSource mDataSource;

    @Inject
    public TicketRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    /**
     * Method to insert entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void insert(Ticket entity) throws IOException {
        mDataSource.insert(entity);
    }

    /**
     * Method to update entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void update(Ticket entity) throws IOException {
        mDataSource.update(entity.getDocId(), entity);
    }

    /**
     * Method to delete entity into repository
     *
     * @param entity Entity object
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public void delete(Ticket entity) throws IOException {
        mDataSource.delete(entity.getDocId(), entity.getClass());
    }

    /**
     * Method to search with specific date the entities
     *
     * @param dateTicket The date object
     * @return The list of entities
     * @throws IOException In case of not being able to access the repository
     */
    @Override
    public List<Ticket> getBySpecificDate(Date dateTicket) throws IOException {
        return mDataSource.getBySpecificDate(dateTicket, Ticket.class).stream().map(Ticket.class::cast).collect(Collectors.toList());
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
    public Ticket getByDocId(String docId) throws IllegalAccessException, IOException {
        return (Ticket) mDataSource.getByDocId(docId, Ticket.class);
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
    public Ticket getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException {
        return (Ticket) mDataSource.getByFriendlyId(friendlyId, Ticket.class);
    }
}
