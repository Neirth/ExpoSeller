/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client;

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
import io.neirth.exposeller.client.peripherals.insertcoins.ButtonInsertCoinsImpl;
import io.neirth.exposeller.client.peripherals.insertcoins.DummyInsertedCoinsImpl;
import io.neirth.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.neirth.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.neirth.exposeller.client.peripherals.ticketgenerator.PassbookTicketGeneratorImpl;
import io.neirth.exposeller.client.repository.datasource.FirebaseDataSourceImpl;
import io.neirth.exposeller.client.repository.datasource.IDataSource;
import io.neirth.exposeller.client.util.Utilities;

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
