package io.smartinez.exposeller.client.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@ActivityScoped
public class TicketRepo implements IRepository<Ticket> {
    private IDataSource mDataSource;

    @Inject
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
    public List<Ticket> getBySpecificDate(Date dateConcerts) {
        return Arrays.asList((Ticket[]) mDataSource.getBySpecificDate(dateConcerts, Ticket.class).toArray());
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
