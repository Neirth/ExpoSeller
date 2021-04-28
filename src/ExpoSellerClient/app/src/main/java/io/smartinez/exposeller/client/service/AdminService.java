package io.smartinez.exposeller.client.service;

import javax.inject.Inject;

import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

import java.io.IOException;

public class AdminService {
    private IDataSource mDataSource;
    private AdBannerRepo mAdBannerRepo;
    private ConcertRepo mConcertRepo;
    private TicketRepo mTicketRepo;

    @Inject
    public AdminService(IDataSource dataSource, AdBannerRepo adBannerRepo, ConcertRepo concertRepo, TicketRepo ticketRepo) {
        this.mDataSource = dataSource;
        this.mAdBannerRepo = adBannerRepo;
        this.mConcertRepo = concertRepo;
        this.mTicketRepo = ticketRepo;
    }

    public void loginAdministrator(String email, String password) throws IOException {
        mDataSource.loginDatabase(email, password);
    }

    public void logoutAdministrator() throws IOException {
        mDataSource.logoutDatabase();
    }

    public AdBannerRepo getAdBannerRepo() {
        return this.mAdBannerRepo;
    }

    public ConcertRepo getConcertRepo() {
        return this.mConcertRepo;
    }
}
