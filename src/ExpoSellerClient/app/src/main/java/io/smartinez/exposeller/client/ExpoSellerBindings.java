package io.smartinez.exposeller.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.pm.PackageManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;
import dagger.internal.DaggerCollections;
import io.smartinez.exposeller.client.peripherals.insertcoins.ButtonInsertCoinsImpl;
import io.smartinez.exposeller.client.peripherals.insertcoins.DummyInsertedCoinsImpl;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.PassbookTicketGeneratorImpl;
import io.smartinez.exposeller.client.repository.datasource.FirebaseDataSourceImpl;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;
import io.smartinez.exposeller.client.util.Utilities;

/**
 * Class managed by Dagger Hilt to provide the dependency injection
 */
@Module
@InstallIn({ ActivityComponent.class, ViewModelComponent.class, SingletonComponent.class })
public abstract class ExpoSellerBindings {

    /**
     * Binding to set the default implementation of ITicketGenerator
     */
    @Binds
    public abstract ITicketGenerator ticketGenerator(PassbookTicketGeneratorImpl passbookTicketGenerator);

    /**
     * Binding to set the default implementation of IDataSource
     */
    @Binds
    public abstract IDataSource dataSource(FirebaseDataSourceImpl firebaseDataSource);

    /**
     * Provider to get a new instance of Executor service
     */
    @Provides
    public static ExecutorService executorService() {
       return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Provider to get a new instance of IInsertCoins
     */
    @Provides
    public static IInsertCoins insertCoins() {
        // Prepare a new variable for IInsertCoins
        IInsertCoins insertCoins = null;

        try {
            // Try to get the application instance
            Application application = Utilities.getApplicationUsingReflection();

            // Get the package manager
            PackageManager pm = application.getApplicationContext().getPackageManager();

            // Detect if this is Android Things or not to select the implementation
            if (pm.hasSystemFeature(PackageManager.FEATURE_EMBEDDED)) {
                insertCoins = new ButtonInsertCoinsImpl();
            } else {
                insertCoins = new DummyInsertedCoinsImpl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the implementation
        return insertCoins;
    }

}
