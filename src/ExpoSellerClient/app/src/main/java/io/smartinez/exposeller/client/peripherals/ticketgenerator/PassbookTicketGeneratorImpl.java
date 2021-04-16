package io.smartinez.exposeller.client.peripherals.ticketgenerator;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ryantenney.passkit4j.Pass;
import com.ryantenney.passkit4j.PassSerializer;
import com.ryantenney.passkit4j.model.Barcode;
import com.ryantenney.passkit4j.model.BarcodeFormat;
import com.ryantenney.passkit4j.model.Color;
import com.ryantenney.passkit4j.model.DateField;
import com.ryantenney.passkit4j.model.EventTicket;
import com.ryantenney.passkit4j.model.TextField;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSignerImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.util.Utilities;

@ActivityScoped
public class PassbookTicketGeneratorImpl implements ITicketGenerator {
    private final TicketType ticketType = TicketType.VIRTUAL;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private ConcertRepo mConcertRepo;

    @Inject
    public PassbookTicketGeneratorImpl(ConcertRepo concertRepo) {
        this.mConcertRepo = concertRepo;
    }

    @Override
    public boolean generatePhysicalTicket(Ticket ticket) throws IOException {
        throw new UnsupportedOperationException("The implementation doesn't support this");
    }

    @Override
    public String generateVirtualTicket(Ticket ticket) throws IOException {
        try {
            // Get concert data
            Concert concert = mConcertRepo.getByDocId(ticket.getDocId());

            // Prepare the resources instance
            Resources andRes = Utilities.getApplicationUsingReflection().getResources();

            // Generate the Passbook ticket
            Pass pass = new Pass()
                    .passTypeIdentifier("io.smartinez.exposeller.ticket")
                    .organizationName(concert.getOrganizationName())
                    .description(concert.getName() + " - " + concert.getArtistName())
                    .serialNumber(concert.getFriendlyId().toString())
                    .relevantDate(concert.getEventDate())
                    .barcode(new Barcode(BarcodeFormat.QR, concert.getFriendlyId().toString()))
                    .backgroundColor(new Color(249, 118, 90))
                    .foregroundColor(Color.BLACK)
                    .passInformation(new EventTicket()
                        .backFields(
                            new TextField("terms", andRes.getString(R.string.terms_and_conditions_name), andRes.getString(R.string.terms_and_conditions_desc))
                        )
                        .primaryFields(
                            new TextField("name", andRes.getString(R.string.event_name), concert.getName())
                        )
                        .secondaryFields(
                            new TextField("artist", andRes.getString(R.string.artist_name), concert.getArtistName()),
                            new DateField("date", andRes.getString(R.string.event_date), concert.getEventDate())
                        )
                    );

            // Prepare the signer
            PassSigner signer = PassSignerImpl.builder()
                                              .keystore(new FileInputStream("assets/certificates/Certificates.p12"), null)
                                              .intermediateCertificate(new FileInputStream("assets/certificates/AppleWWDRCA.cer"))
                                              .build();

            // Get the reference to file
            StorageReference ticketCloudUri = storageRef.child(UUID.randomUUID().toString() + "/" + UUID.randomUUID().toString() + ".pkpass");

            // Prepare the buffer to receive the ticket file
            try (ByteArrayOutputStream outputFile = new ByteArrayOutputStream()){
                // Write to buffer the generated ticket
                PassSerializer.writePkPassArchive(pass, signer, outputFile);

                // Flush the buffer
                outputFile.flush();

                // Upload bytes to file
                ticketCloudUri.putStream(new ByteArrayInputStream(outputFile.toByteArray()));
            }

            // Return the path.
            return ticketCloudUri.getDownloadUrl().getResult().getPath();
        } catch (Exception e) {
            Log.e("PassbookTicket", "No could generate the ticket");
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String generateHybridTicket(Ticket ticket) throws IOException {
        throw new UnsupportedOperationException("The implementation doesn't support this");
    }

    @Override
    public TicketType getImplementationType() {
        return ticketType;
    }
}
