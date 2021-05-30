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
