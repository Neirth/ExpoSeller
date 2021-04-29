package io.smartinez.exposeller.checker.service;

import io.smartinez.exposeller.checker.peripheral.validator.IValidator;
import io.smartinez.exposeller.checker.peripheral.friendlyidreader.IFriendlyIdReader;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ValidatorService {
    private IValidator mValidator;
    private IFriendlyIdReader mFriendlyIdReader;

    @Inject
    public ValidatorService(IValidator validator, IFriendlyIdReader friendlyIdReader) {
        this.mValidator = validator;
        this.mFriendlyIdReader = friendlyIdReader;
    }

    public void startValidator(Runnable successCallback, Runnable errorCallback) {
        mFriendlyIdReader.detectFriendlyId(friendlyId -> {
            boolean isValidated = mValidator.checkFriendlyId(friendlyId);

            if (isValidated) {
                successCallback.run();
            } else {
                errorCallback.run();
            }
        });
    }
}
