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
