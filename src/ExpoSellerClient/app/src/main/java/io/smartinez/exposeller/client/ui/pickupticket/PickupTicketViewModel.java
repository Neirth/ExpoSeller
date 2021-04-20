package io.smartinez.exposeller.client.ui.pickupticket;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.smartinez.exposeller.client.service.UserService;

public class PickupTicketViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public PickupTicketViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }

    public void generateTicketQrCode(Context context, ImageView imageView, Uri uriTicket) {
        QRGEncoder qrgEncoder = new QRGEncoder(uriTicket.getPath(), null, QRGContents.Type.TEXT, 128);

        Glide.with(context).load(qrgEncoder.getBitmap()).into(imageView);
    }

}
