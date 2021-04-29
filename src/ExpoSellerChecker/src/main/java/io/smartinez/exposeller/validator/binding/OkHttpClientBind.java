package io.smartinez.exposeller.validator.binding;

import jakarta.enterprise.inject.Produces;
import okhttp3.OkHttpClient;

public class OkHttpClientBind {
    @Produces
    public okhttp3.OkHttpClient producesOkHttp() {
        return new okhttp3.OkHttpClient();
    }
}
