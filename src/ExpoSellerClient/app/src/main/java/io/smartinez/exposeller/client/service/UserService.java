package io.smartinez.exposeller.client.service;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.util.Utilities;

public class UserService {

    private final TicketRepo mTicketRepo;
    private ITicketGenerator mTicketGenerator;
    private IInsertCoins mInsertCoins;
    private AdBannerRepo mAdBannerRepo;
    private ConcertRepo mConcertRepo;

    @Inject
    public UserService(ITicketGenerator ticketGenerator, IInsertCoins insertCoins, TicketRepo ticketRepo, ConcertRepo concertRepo, AdBannerRepo adBannerRepo) {
        this.mTicketGenerator = ticketGenerator;
        this.mInsertCoins = insertCoins;
        this.mTicketRepo = ticketRepo;
        this.mConcertRepo = concertRepo;
        this.mAdBannerRepo = adBannerRepo;
    }

    public LiveData<Float> checkInsertCoins() throws IOException {
        return mInsertCoins.checkInsertedValue();
    }

    public boolean returnValueCoins(float desiredValue, float giveValue) {
        return mInsertCoins.returnExcessAmount(desiredValue, giveValue);
    }

    public void buyTicket(Ticket ticket) throws IOException {
        int friendlyId = Utilities.generateRandomFriendlyId();

        try {
            while (true) {
                mTicketRepo.getByFriendlyId(String.valueOf(friendlyId));
                friendlyId = Utilities.generateRandomFriendlyId();
            }
        } catch (IllegalAccessException e) {
            ticket.setFriendlyId(friendlyId);
        }

        mTicketRepo.insert(ticket);

        switch (mTicketGenerator.getImplementationType()) {
            case HYBRID:
                mTicketGenerator.generateHybridTicket(ticket);
                break;
            case VIRTUAL:
                mTicketGenerator.generateVirtualTicket(ticket);
                break;
            case PHYSICAL:
                mTicketGenerator.generatePhysicalTicket(ticket);
                break;
        }
    }

    public List<AdBanner> pickRandomAdsList() {
        Random randomGen = new Random();

        List<AdBanner> adBanners = mAdBannerRepo.getNotBeforeDate(new Date());
        List<AdBanner> randomAdBanners = Collections.emptyList();

        if (!adBanners.isEmpty()) {
            Collections.shuffle(adBanners);

            for (int i = 0; i < 5; i++) {
                int randomIndex = randomGen.nextInt(adBanners.size());

                randomAdBanners.add(adBanners.get(randomIndex));
                adBanners.remove(randomIndex);
            }
        }

        return randomAdBanners;
    }

    public List<Concert> readConcertList() {
        return mConcertRepo.getNotBeforeDate(new Date());
    }

    public List<Concert> searchConcertWithDay(Date date) {
        return mConcertRepo.getBySpecificDate(date);
    }
}
