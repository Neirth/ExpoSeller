package io.smartinez.exposeller.validator.peripheral.friendlyidreader;

import io.smartinez.exposeller.validator.listener.OnScannedCodeListener;

public interface IFriendlyIdReader {
    void detectFriendlyId(OnScannedCodeListener onScannedCodeListener);
}
