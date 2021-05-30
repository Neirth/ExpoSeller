/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    /**
     * Wrapper to IInsertCoins method to check the insert value
     *
     * @return Observable of inserted value
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    public Observable checkInsertCoins() throws IOException {
        return mInsertCoins.checkInsertedValue();
    }

    /**
     * Wrapper to IInsertCoins method to return the inserted value
     *
     * @return The state of operation
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    public boolean returnValueCoins(float desiredValue) throws IOException {
        return mInsertCoins.returnExcessAmount(desiredValue);
    }

    /**
     * Method to buy and get a ticket
     *
     * @param ticket The ticket object
     * @param checkoutComplete The checkoutComplete callback
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    public void buyTicket(Ticket ticket, CheckoutCompleteListener checkoutComplete) throws IOException {
        // Insert the ticket in the database
        mTicketRepo.insert(ticket);

        // Prepare the result variable
        String result = "";

        // Get the implementation type
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

        // Execute the callback of checkout complete
        checkoutComplete.checkoutComplete(result);
    }

    /**
     * Method to pick random advertisement list
     *
     * @return The shuffled advertisement list
     * @throws IOException In case of not being able to access the database
     */
    public List<Advertisement> pickRandomAdsList() throws IOException {
        List<Advertisement> advertisements = mAdvertisementRepo.getNotBeforeDate(new Date());

        Collections.shuffle(advertisements);

        return advertisements;
    }

    /**
     * Method to read the concert list
     *
     * @return The concert list
     * @throws IOException In case of not being able to access the database
     */
    public List<Concert> readConcertList() throws IOException {
        return mConcertRepo.getNotBeforeDate(new Date());
    }

    /**
     * Method to search concert in a specific date
     *
     * @param date The date for search
     * @return The concert list
     * @throws IOException In case of not being able to access the database
     */
    public List<Concert> searchConcertWithDay(Date date) throws IOException {
        return mConcertRepo.getBySpecificDate(date);
    }

    /**
     * Method for generate the friendly id for tickets
     *
     * @return The free friendly id
     * @throws IOException In case of not being able to access the database
     */
    public int generateTicketFriendlyId() throws IOException {
        // Prepare the friendlyId
        int friendlyId = 0;

        try {
            while (true) {
                // Generate a random friendly id
                friendlyId = Utilities.generateRandomFriendlyId();

                // Check if no exist, if no exist, the method throw a exception.
                mTicketRepo.getByFriendlyId(String.valueOf(friendlyId));
            }
        } catch (IllegalAccessException e) {
            // INFO: Ignoring the consequences because the value is free
        }

        // Return the value
        return friendlyId;
    }

    /**
     * Method to recover the implementation type of ticket generator
     *
     * @return The ticket generator implementation type
     */
    public TicketType getTicketImplType() {
        return mTicketGenerator.getImplementationType();
    }
}
