package io.smartinez.exposeller.validator.service;

import io.smartinez.exposeller.validator.peripheral.validator.IValidator;
import io.smartinez.exposeller.validator.peripheral.friendlyidreader.IFriendlyIdReader;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidatorService {
    private final IValidator mValidator;
    private final IFriendlyIdReader mFriendlyIdReader;

    @Inject
    public ValidatorService(IValidator validator, IFriendlyIdReader friendlyIdReader) {
        this.mValidator = validator;
        this.mFriendlyIdReader = friendlyIdReader;
    }

    public void startValidator(Runnable successCallback, Runnable errorCallback, Runnable resetCallback) {
        mFriendlyIdReader.detectFriendlyId(friendlyId -> {
            boolean isValidated = mValidator.checkFriendlyId(friendlyId);

            if (isValidated) {
                successCallback.run();
            } else {
                errorCallback.run();
            }

            Thread.sleep(2000);
            resetCallback.run();
        });
    }
}
