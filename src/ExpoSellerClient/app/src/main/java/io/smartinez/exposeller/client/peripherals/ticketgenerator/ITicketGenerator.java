package io.smartinez.exposeller.client.peripherals.ticketgenerator;

import java.io.IOException;

import io.smartinez.exposeller.client.domain.Ticket;

public interface ITicketGenerator {
    /**
     * Method to convert a Ticket Object to Physical Ticket Object
     *
     * @param ticket The ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    void generatePhysicalTicket(Ticket ticket) throws IOException;

    /**
     * Method to convert Ticket Object to Virtual Ticket Object
     *
     * @param ticket The ticket
     * @return The uri from virtual ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    String generateVirtualTicket(Ticket ticket) throws IOException;

    /**
     * Method to convert a Ticket Object to Physical Ticket Object and Virtual Ticket Object
     *
     * @param ticket The ticket
     * @return The uri from virtual ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    String generateHybridTicket(Ticket ticket) throws IOException;

    /**
     * Method to obtain the type of implementation we are using
     *
     * @return The implementation type
     */
    TicketType getImplementationType();
}
