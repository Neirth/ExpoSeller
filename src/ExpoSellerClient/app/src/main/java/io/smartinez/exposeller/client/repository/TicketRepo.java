package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

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
