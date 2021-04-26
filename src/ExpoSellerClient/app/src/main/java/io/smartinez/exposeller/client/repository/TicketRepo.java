package io.smartinez.exposeller.client.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@ViewModelScoped
public class TicketRepo implements IRepository<Ticket> {
    private IDataSource mDataSource;

    @Inject
    public TicketRepo(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public void insert(Ticket entity) throws IOException {
        mDataSource.insert(entity);
    }

    @Override
    public void update(Ticket entity) throws IOException {
        mDataSource.update(entity.getDocId(), entity);
    }

    @Override
    public void delete(Ticket entity) throws IOException {
        mDataSource.delete(entity.getDocId(), entity.getClass());
    }

    @Override
    public List<Ticket> getBySpecificDate(Date dateConcerts) throws IOException {
        return Arrays.asList((Ticket[]) mDataSource.getBySpecificDate(dateConcerts, Ticket.class).toArray());
    }

    @Override
    public Ticket getByDocId(String docId) throws IllegalAccessException, IOException {
        return (Ticket) mDataSource.getByDocId(docId, Ticket.class);
    }

    @Override
    public Ticket getByFriendlyId(String friendlyId) throws IllegalAccessException, IOException {
        return (Ticket) mDataSource.getByFriendlyId(friendlyId, Ticket.class);
    }
}
