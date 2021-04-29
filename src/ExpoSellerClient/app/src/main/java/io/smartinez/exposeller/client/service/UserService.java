package io.smartinez.exposeller.client.service;

import java.io.IOException;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.TicketType;
import io.smartinez.exposeller.client.repository.AdvertisementRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.util.Utilities;
import io.smartinez.exposeller.client.util.listener.CheckoutCompleteListener;

@Singleton
public class UserService {
    private final TicketRepo mTicketRepo;
    private ITicketGenerator mTicketGenerator;
    private IInsertCoins mInsertCoins;
    private AdvertisementRepo mAdvertisementRepo;
    private ConcertRepo mConcertRepo;

    @Inject
    public UserService(ITicketGenerator ticketGenerator, IInsertCoins insertCoins, TicketRepo ticketRepo, ConcertRepo concertRepo, AdvertisementRepo advertisementRepo) {
        this.mTicketGenerator = ticketGenerator;
        this.mInsertCoins = insertCoins;
        this.mTicketRepo = ticketRepo;
        this.mConcertRepo = concertRepo;
        this.mAdvertisementRepo = advertisementRepo;
    }

    public Observable checkInsertCoins() throws IOException {
        return mInsertCoins.checkInsertedValue();
    }

    public boolean returnValueCoins(float desiredValue) throws IOException {
        return mInsertCoins.returnExcessAmount(desiredValue);
    }

    public boolean returnValueCoins(float desiredValue, float giveValue) throws IOException {
        return mInsertCoins.returnExcessAmount(desiredValue, giveValue);
    }

    public void buyTicket(Ticket ticket, CheckoutCompleteListener checkoutComplete) throws IOException {
        int friendlyId = generateTicketFriendlyId();

        mTicketRepo.insert(ticket);

        String result = "";

        switch (mTicketGenerator.getImplementationType()) {
            case HYBRID:
                result = mTicketGenerator.generateHybridTicket(ticket);
                break;
            case VIRTUAL:
                result = mTicketGenerator.generateVirtualTicket(ticket);
                break;
            case PHYSICAL:
                mTicketGenerator.generatePhysicalTicket(ticket);
                break;
        }

        checkoutComplete.checkoutComplete(result);
    }

    public List<Advertisement> pickRandomAdsList() throws IOException {
        List<Advertisement> advertisements = mAdvertisementRepo.getNotBeforeDate(new Date());
        List<Advertisement> randomAdvertisements = new ArrayList<>();

        if (!advertisements.isEmpty()) {
            Collections.shuffle(advertisements);

            int listSize = (advertisements.size() >= 5) ? 5 : advertisements.size();

            for (int i = 0; i < listSize; i++) {
                randomAdvertisements.add(advertisements.get(i));
                advertisements.remove(i);
            }
        }

        return randomAdvertisements;
    }

    public List<Concert> readConcertList() throws IOException {
        return mConcertRepo.getNotBeforeDate(new Date());
    }

    public List<Concert> searchConcertWithDay(Date date) throws IOException {
        return mConcertRepo.getBySpecificDate(date);
    }

    public int generateTicketFriendlyId() throws IOException {
        int friendlyId = 0;

        try {
            while (true) {
                friendlyId = Utilities.generateRandomFriendlyId();

                mTicketRepo.getByFriendlyId(String.valueOf(friendlyId));
            }
        } catch (IllegalAccessException e) {
            // Ignoring the consequences because the value is free
        }

        return friendlyId;
    }

    public TicketType getTicketImplType() {
        return mTicketGenerator.getImplementationType();
    }
}
