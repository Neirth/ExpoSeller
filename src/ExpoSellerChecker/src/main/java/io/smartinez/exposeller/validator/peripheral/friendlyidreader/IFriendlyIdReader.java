package io.smartinez.exposeller.checker.peripheral.friendlyidreader;

import io.smartinez.exposeller.checker.util.listener.OnScannedCodeListener;

public interface IFriendlyIdReader {
    void detectFriendlyId(OnScannedCodeListener onScannedCodeListener);
}
