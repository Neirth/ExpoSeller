package io.smartinez.exposeller.client.util.listener;

import java.io.IOException;

public interface CheckoutCompleteListener {
    void checkoutComplete(String uriTicket) throws IOException;
}
