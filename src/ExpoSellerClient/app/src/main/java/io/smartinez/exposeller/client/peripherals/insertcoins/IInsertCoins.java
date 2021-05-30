package io.smartinez.exposeller.client.peripherals.insertcoins;

import java.io.IOException;
import java.util.Observable;

public interface IInsertCoins {
    /**
     * Observable to check the inserted value in the system.
     *
     * @return The observable of checkInsertValue
     * @throws IOException Exception in case you cannot initialize the components.
     */
    Observable checkInsertedValue() throws IOException;

    /**
     * Method to return the inserted value in the system.
     *
     * @param desiredValue The desired value to return.
     * @return The result of operation.
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    boolean returnExcessAmount(float desiredValue) throws IOException;

    /**
     * Method to return the value desired in the system.
     *
     * @param desiredValue The desired value to return.
     * @param giveValue The give value.
     * @return The result of operation.
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException;

}
