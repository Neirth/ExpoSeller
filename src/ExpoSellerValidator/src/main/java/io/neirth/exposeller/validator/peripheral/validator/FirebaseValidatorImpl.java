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
package io.neirth.exposeller.validator.peripheral.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@ApplicationScoped
@SuppressWarnings("unused")
public class FirebaseValidatorImpl implements IValidator {
    // Base Url
    private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/";

    // MediaType of JSON
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    // Instance of properties
    private final Properties mProperties;

    // Instance of OkHttp
    private final OkHttpClient mOkHttpClient;

    @Inject
    public FirebaseValidatorImpl(OkHttpClient okHttpClient, Properties properties) {
        this.mOkHttpClient = okHttpClient;
        this.mProperties = properties;
    }

    /**
     * Method for check if the ticket with give friendlyId is valid
     *
     * @param friendlyId The friendly id for the ticket
     * @return The validation status
     */
    @Override
    public boolean checkFriendlyId(String friendlyId) {
        JsonNode document = getTicketDocument(friendlyId);

        // Check if not is used the ticket
        if (document != null && !document.get("fields").get("used").get("booleanValue").asBoolean()) {
            // Mark as used
            updateTicketDocument(friendlyId);

            // Return that is valid ticket
            return true;
        } else {
            // Return that is not valid ticket
            return false;
        }
    }

    /**
     * Private method to update documents from firestore
     *
     * @param friendlyId The friendly id for the ticket
     */
    private void updateTicketDocument(String friendlyId) {
        try {
            // Get the document ticket as JsonNode
            JsonNode document = getTicketDocument(friendlyId);

            // Get the document path and get the document id
            String[] documentPath = Objects.requireNonNull(document).get("name").asText().split("/");
            String documentId = documentPath[documentPath.length - 1];

            // Set the params to build the request
            String queryUrl = BASE_URL + mProperties.getProperty("FIREBASE_PROJECT_ID") + "/databases/(default)/documents/Ticket/" + documentId + "?updateMask.fieldPaths=used";
            String body = "{\"fields\":{\"used\":{\"booleanValue\":true}}}";

            // Build the request
            Request request = new Request.Builder().url(queryUrl).patch(RequestBody.create(body, MEDIA_TYPE)).build();

            // Execute the request
            mOkHttpClient.newCall(request).execute();
        } catch (IOException ignored) {
        }
    }

    /**
     * Private method to get the ticket with friendly id
     *
     * @param friendlyId The friendly id for the ticket
     * @return Ticket as a json node
     */
    private JsonNode getTicketDocument(String friendlyId) {
        // Set the params to build the request
        String queryUrl = BASE_URL + mProperties.getProperty("FIREBASE_PROJECT_ID") + "/databases/(default)/documents:runQuery";
        String body = "{\"structuredQuery\":{\"where\":{\"fieldFilter\":{\"field\":{\"fieldPath\":\"friendlyId\"},\"op\":\"EQUAL\",\"value\":{\"integerValue\":" + friendlyId + "}}},\"from\":[{\"collectionId\":\"Ticket\"}]}}";

        try {
            // Build the request
            Request request = new Request.Builder().url(queryUrl).post(RequestBody.create(body, MEDIA_TYPE)).build();

            // Execute the request
            Response response = mOkHttpClient.newCall(request).execute();

            // Get a JsonNode array
            JsonNode[] object = new ObjectMapper().readValue(Objects.requireNonNull(response.body()).string(), JsonNode[].class);

            // Return the array
            return object[0].get("document");
        } catch (IOException e) {
            return null;
        }
    }
}
