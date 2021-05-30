package io.smartinez.exposeller.client.util.listener;

import java.io.IOException;

/**
 * Callback to checkout complete event
 */
public interface CheckoutCompleteListener {
    void checkoutComplete(String uriTicket) throws IOException;
}
