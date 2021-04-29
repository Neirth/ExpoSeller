package io.smartinez.exposeller.validator.peripheral.validator;

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
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/";

    private final OkHttpClient mOkHttpClient;
    private final Properties mProperties;

    @Inject
    public FirebaseValidatorImpl(OkHttpClient okHttpClient, Properties properties) {
        this.mOkHttpClient = okHttpClient;
        this.mProperties = properties;
    }

    @Override
    public boolean checkFriendlyId(String friendlyId) {
        JsonNode document = getTicketDocument(friendlyId);

        if (document != null) {
            if (!document.get("fields").get("used").get("booleanValue").asBoolean()) {
                updateTicketDocument(friendlyId);

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void updateTicketDocument(String friendlyId) {
        try {
            JsonNode document = getTicketDocument(friendlyId);

            String[] documentPath = Objects.requireNonNull(document).get("name").asText().split("/");
            String documentId = documentPath[documentPath.length - 1];

            String queryUrl = BASE_URL + mProperties.getProperty("FIREBASE_PROJECT_ID") + "/databases/(default)/documents/Ticket/" + documentId + "?updateMask.fieldPaths=used";
            String body = "{\"fields\":{\"used\":{\"booleanValue\":true}}}";

            Request request = new Request.Builder().url(queryUrl).patch(RequestBody.create(body, MEDIA_TYPE)).build();

            mOkHttpClient.newCall(request).execute();
        } catch (IOException ignored) {
        }
    }

    private JsonNode getTicketDocument(String friendlyId) {
        String queryUrl = BASE_URL + mProperties.getProperty("FIREBASE_PROJECT_ID") + "/databases/(default)/documents:runQuery";
        String body = "{\"structuredQuery\":{\"where\":{\"fieldFilter\":{\"field\":{\"fieldPath\":\"friendlyId\"},\"op\":\"EQUAL\",\"value\":{\"integerValue\":" + friendlyId + "}}},\"from\":[{\"collectionId\":\"Ticket\"}]}}";

        try {
            Request request = new Request.Builder().url(queryUrl).post(RequestBody.create(body, MEDIA_TYPE)).build();

            Response response = mOkHttpClient.newCall(request).execute();
            JsonNode[] object = new ObjectMapper().readValue(Objects.requireNonNull(response.body()).string(), JsonNode[].class);

            return object[0].get("document");
        } catch (IOException e) {
            return null;
        }
    }
}
