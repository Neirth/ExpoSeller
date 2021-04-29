package io.smartinez.exposeller.validator.binding;

import jakarta.enterprise.inject.Produces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class ExecutorServiceBind {
    @Produces
    public ExecutorService provideExecutionService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
