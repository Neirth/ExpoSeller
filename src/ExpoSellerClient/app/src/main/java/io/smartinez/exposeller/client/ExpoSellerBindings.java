package io.smartinez.exposeller.client;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import io.smartinez.exposeller.client.peripherals.insertcoins.ButtonInsertCoinsImpl;
import io.smartinez.exposeller.client.peripherals.insertcoins.IInsertCoins;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.ITicketGenerator;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.PassbookTicketGeneratorImpl;

@Module
@InstallIn(ActivityComponent.class)

public abstract class ExpoSellerBindings {
    @Binds
    public abstract ITicketGenerator ticketGenerator(PassbookTicketGeneratorImpl passbookTicketGenerator);

    @Binds
    public abstract IInsertCoins insertCoins(ButtonInsertCoinsImpl buttonInsertCoins);
}
