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
package io.neirth.exposeller.validator.service;

import io.neirth.exposeller.validator.peripheral.friendlyidreader.IFriendlyIdReader;
import io.neirth.exposeller.validator.peripheral.validator.IValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidatorService {
    // Instance of validator
    private final IValidator mValidator;

    // Instance of friendly id reader
    private final IFriendlyIdReader mFriendlyIdReader;

    @Inject
    public ValidatorService(IValidator validator, IFriendlyIdReader friendlyIdReader) {
        this.mValidator = validator;
        this.mFriendlyIdReader = friendlyIdReader;
    }

    /**
     * Method to start the validation use case
     *
     * @param successCallback Success callback
     * @param errorCallback Error callback
     * @param resetCallback Reset callback
     */
    public void startValidator(Runnable successCallback, Runnable errorCallback, Runnable resetCallback) {
        mFriendlyIdReader.detectFriendlyId(friendlyId -> {
            // Check if friendly id is valid or not
            if (mValidator.checkFriendlyId(friendlyId)) {
                successCallback.run();
            } else {
                errorCallback.run();
            }

            // Wait two seconds before execute other operation
            Thread.sleep(2000);

            // Call to reset callback
            resetCallback.run();
        });
    }
}
