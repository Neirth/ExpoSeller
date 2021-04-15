package io.smartinez.exposeller.client.peripherals.ticketgenerator;

import java.io.IOException;

import io.smartinez.exposeller.client.domain.Ticket;

public interface ITicketGenerator {
    boolean generatePhysicalTicket(Ticket ticket) throws IOException;
    String generateVirtualTicket(Ticket ticket) throws IOException;
    String generateHybridTicket(Ticket ticket) throws IOException;

    TicketType getImplementationType();
}
