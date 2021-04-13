package io.smartinez.exposeller.client.repository;

import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

public class TicketRepo implements IRepository<Ticket> {
    private IDataSource mDataSource;

    public TicketRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public void insert(Ticket entity) {
        mDataSource.insert(entity);
    }

    @Override
    public void update(Ticket entity) {
        mDataSource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(Ticket entity) {
        mDataSource.delete(entity.getDocId(), entity.getClass());
    }

    @Override
    public Ticket getByDocId(String docId) throws IllegalAccessException {
        return (Ticket) mDataSource.getByDocId(docId, Ticket.class);
    }

    @Override
    public Ticket getByFriendlyId(String friendlyId) throws IllegalAccessException {
        return (Ticket) mDataSource.getByFriendlyId(friendlyId, Ticket.class);
    }
}
