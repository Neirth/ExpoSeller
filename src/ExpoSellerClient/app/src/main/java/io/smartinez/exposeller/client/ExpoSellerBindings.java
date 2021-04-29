package io.smartinez.exposeller.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;
import io.smartinez.exposeller.client.peripherals.insertcoins.ButtonInsertCoinsImpl;
import io.smartinez.exposeller.client.peripherals.insertcoins.DummyInsertedCoinsImpl;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.PassbookTicketGeneratorImpl;
import io.smartinez.exposeller.client.repository.datasource.FirebaseDataSourceImpl;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

@Module
@InstallIn({ ActivityComponent.class, ViewModelComponent.class, SingletonComponent.class })
public abstract class ExpoSellerBindings {
    @Binds
    public abstract ITicketGenerator ticketGenerator(PassbookTicketGeneratorImpl passbookTicketGenerator);

    @Binds
    public abstract IInsertCoins insertCoins(DummyInsertedCoinsImpl buttonInsertCoins);

    @Binds
    public abstract IDataSource dataSource(FirebaseDataSourceImpl firebaseDataSource);

    @Provides
    public static ExecutorService executorService() {
       return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
