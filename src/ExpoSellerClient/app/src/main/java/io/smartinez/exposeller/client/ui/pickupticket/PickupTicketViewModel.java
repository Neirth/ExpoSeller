package io.smartinez.exposeller.client.ui.pickupticket;

import android.content.Context;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.TicketType;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class PickupTicketViewModel extends ViewModel {
    // Instance for use case user
    private UserService mUsersService;

    @Inject
    public PickupTicketViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }

    /**
     * Method to show the qr code with uri
     *
     * @param context The activity context
     * @param imageView The image view element
     * @param uriTicket The uri for the ticket
     */
    public void generateTicketQrCode(Context context, ImageView imageView, String uriTicket) {
        // Generate a new QR code
        QRGEncoder qrgEncoder = new QRGEncoder(uriTicket, null, QRGContents.Type.TEXT, 128);

        // Load it into image view
        Glide.with(context).load(qrgEncoder.getBitmap()).into(imageView);
    }

    /**
     * Method to get the type of implementation of ITicketGenerator
     *
     * @return The type of implementation
     */
    public TicketType getTicketImplType() {
        return mUsersService.getTicketImplType();
    }
}
