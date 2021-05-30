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
