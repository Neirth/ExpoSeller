package io.smartinez.exposeller.client.service;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.texttospeech.ITextToSpeech;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.util.Utilities;
import io.smartinez.exposeller.client.util.listener.CheckoutCompleteListener;

@ViewModelScoped
public class UserService {
    private final TicketRepo mTicketRepo;
    private ITicketGenerator mTicketGenerator;
    private IInsertCoins mInsertCoins;
    private AdBannerRepo mAdBannerRepo;
    private ConcertRepo mConcertRepo;
    private ITextToSpeech mTextToSpeech;

    @Inject
    public UserService(ITicketGenerator ticketGenerator, IInsertCoins insertCoins, TicketRepo ticketRepo, ConcertRepo concertRepo, AdBannerRepo adBannerRepo, ITextToSpeech mTextToSpeech) {
        this.mTicketGenerator = ticketGenerator;
        this.mInsertCoins = insertCoins;
        this.mTicketRepo = ticketRepo;
        this.mConcertRepo = concertRepo;
        this.mAdBannerRepo = adBannerRepo;
        this.mTextToSpeech = mTextToSpeech;
    }

    public LiveData<Float> checkInsertCoins() throws IOException {
        return mInsertCoins.checkInsertedValue();
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

    public List<AdBanner> pickRandomAdsList() throws IOException {
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

    public void speak(String ttsString) {
        mTextToSpeech.speak(ttsString);
    }
}
