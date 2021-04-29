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
        return mDataSource.getBySpecificDate(dateConcerts, Ticket.class).stream().map(Ticket.class::cast).collect(Collectors.toList());
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
