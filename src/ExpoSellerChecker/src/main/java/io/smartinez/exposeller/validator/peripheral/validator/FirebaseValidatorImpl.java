package io.smartinez.exposeller.checker.peripheral.validator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class FirebaseValidatorImpl implements IValidator {
    @Inject
    public FirebaseValidatorImpl() {
    }

    @Override
    public boolean checkFriendlyId(String friendlyId) {
        return false;
    }
}
