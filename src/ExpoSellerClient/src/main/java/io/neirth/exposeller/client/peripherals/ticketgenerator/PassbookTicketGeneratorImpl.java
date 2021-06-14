/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.peripherals.ticketgenerator;

import android.content.res.Resources;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ryantenney.passkit4j.Pass;
import com.ryantenney.passkit4j.PassResource;
import com.ryantenney.passkit4j.PassSerializer;
import com.ryantenney.passkit4j.model.Barcode;
import com.ryantenney.passkit4j.model.BarcodeFormat;
import com.ryantenney.passkit4j.model.Color;
import com.ryantenney.passkit4j.model.EventTicket;
import com.ryantenney.passkit4j.model.TextField;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSignerImpl;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Concert;
import io.neirth.exposeller.client.domain.Ticket;
import io.neirth.exposeller.client.repository.ConcertRepo;
import io.neirth.exposeller.client.util.Utilities;

@Singleton
public class PassbookTicketGeneratorImpl implements ITicketGenerator {
    private final TicketType ticketType;
    private final StorageReference storageRef;

    private final ConcertRepo mConcertRepo;
    private final BlockingQueue<Boolean> mThreadBus;

    @Inject
    public PassbookTicketGeneratorImpl(ConcertRepo concertRepo) {
        this.ticketType = TicketType.VIRTUAL;

        this.mConcertRepo = concertRepo;
        this.mThreadBus = new LinkedBlockingDeque<>();
        this.storageRef = FirebaseStorage.getInstance().getReference().child(Ticket.class.getSimpleName());
    }

    /**
     * Method to convert a Ticket Object to Physical Ticket Object
     *
     * @param ticket The ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    @Override
    public void generatePhysicalTicket(Ticket ticket) throws IOException {
        throw new UnsupportedOperationException("The implementation doesn't support this");
    }

    /**
     * Method to convert Ticket Object to Virtual Ticket Object
     *
     * @param ticket The ticket
     * @return The uri from virtual ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    @Override
    public String generateVirtualTicket(Ticket ticket) throws IOException {
        try {
            // Get concert data
            Concert concert = mConcertRepo.getByDocId(ticket.getConcertId());

            // Prepare the resources instance
            Resources andRes = Utilities.getApplicationUsingReflection().getResources();

            // Get classloader from thread
            ClassLoader classLoader = getClass().getClassLoader();

            // Prepare date formatter
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
            LocalDateTime dateConcertObj = concert.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Generate the Passbook ticket
            Pass pass = new Pass()
                .passTypeIdentifier("io.smartinez.exposeller.ticket")
                .organizationName(concert.getOrganizationName())
                .description(concert.getName() + " - " + concert.getArtistName())
                .serialNumber(ticket.getFriendlyId().toString())
                .relevantDate(concert.getEventDate())
                .barcode(new Barcode(BarcodeFormat.QR, ticket.getFriendlyId().toString()))
                .labelColor(new Color(0, 0 ,0))
                .files(
                    new PassResource("logo.png", classLoader.getResourceAsStream("assets/eventticket/logo.png")),
                    new PassResource("logo@2x.png", classLoader.getResourceAsStream("assets/eventticket/logo@2x.png")),
                    new PassResource("background.png", classLoader.getResourceAsStream("assets/eventticket/background.png")),
                    new PassResource("background@2x.png", classLoader.getResourceAsStream("assets/eventticket/background@2x.png"))
                )
                .passInformation(new EventTicket()
                    .backFields(
                        new TextField("terms", andRes.getString(R.string.terms_and_conditions_name), andRes.getString(R.string.terms_and_conditions_desc))
                    )
                    .primaryFields(
                        new TextField("name", andRes.getString(R.string.event_name), concert.getName())
                    )
                    .auxiliaryFields(
                        new TextField("artist", andRes.getString(R.string.artist_name), concert.getArtistName()),
                        new TextField("date", andRes.getString(R.string.event_date), dateConcertObj.format(dateFormatter))
                    )
                );

            // Prepare the signer
            PassSigner signer = PassSignerImpl.builder()
                .keystore(classLoader.getResourceAsStream("assets/certificates/Certificates.p12"), null)
                .intermediateCertificate(classLoader.getResourceAsStream("assets/certificates/AppleWWDRCA.cer"))
                .build();

            // Get the reference to file
            StorageReference ticketCloudUri = storageRef.child(UUID.randomUUID().toString())
                .child(UUID.randomUUID().toString() + ".pkpass");

            // Prepare the buffer to receive the ticket file
            try (ByteArrayOutputStream outputFile = new ByteArrayOutputStream()) {
                // Write to buffer the generated ticket
                PassSerializer.writePkPassArchive(pass, signer, outputFile);

                // Flush the buffer
                outputFile.flush();

                // Upload bytes to file
               UploadTask uploadTask = ticketCloudUri.putStream(new ByteArrayInputStream(outputFile.toByteArray()));

               // Wait to upload
               uploadTask.addOnCompleteListener(command -> mThreadBus.add(true));
               mThreadBus.take();
            }

            // Get the download url
            Task<Uri> uriTask = ticketCloudUri.getDownloadUrl();

            // Wait to task
            uriTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            if (uriTask.isSuccessful()) {
                // Return the path.
                return uriTask.getResult().toString();
            } else {
                throw new IOException("No could get the uri ticket");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Method to convert a Ticket Object to Physical Ticket Object and Virtual Ticket Object
     *
     * @param ticket The ticket
     * @return The uri from virtual ticket
     * @throws IOException Exception in case you cannot initialize the components
     */
    @Override
    public String generateHybridTicket(Ticket ticket) throws IOException {
        throw new UnsupportedOperationException("The implementation doesn't support this");
    }

    /**
     * Method to obtain the type of implementation we are using
     *
     * @return The implementation type
     */
    @Override
    public TicketType getImplementationType() {
        return ticketType;
    }
}
