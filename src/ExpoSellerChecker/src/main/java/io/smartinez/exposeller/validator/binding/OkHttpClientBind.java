package io.smartinez.exposeller.validator.binding;

import jakarta.enterprise.inject.Produces;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class OkHttpClientBind {
    @Produces
    public okhttp3.OkHttpClient producesOkHttp() {
        return new OkHttpClient().newBuilder().connectTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS).readTimeout(2, TimeUnit.SECONDS)
                .build();
    }
}
