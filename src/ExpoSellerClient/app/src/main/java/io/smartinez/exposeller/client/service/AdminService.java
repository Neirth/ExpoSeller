package io.smartinez.exposeller.client.service;

import javax.inject.Inject;

import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;

public class AdminService {

    private AdBannerRepo mAdBannerRepo;
    private ConcertRepo mConcertRepo;
    private TicketRepo mTicketRepo;

    @Inject
    public AdminService(AdBannerRepo adBannerRepo, ConcertRepo concertRepo, TicketRepo ticketRepo) {
       this.mAdBannerRepo = adBannerRepo;
       this.mConcertRepo = concertRepo;
       this.mTicketRepo = ticketRepo;
    }
}
