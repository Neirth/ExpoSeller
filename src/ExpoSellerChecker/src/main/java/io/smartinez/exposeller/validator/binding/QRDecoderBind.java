package io.smartinez.exposeller.validator.binding;

import jakarta.enterprise.inject.Produces;
import nu.pattern.OpenCV;
import org.opencv.objdetect.QRCodeDetector;

@SuppressWarnings("unused")
public class QRDecoderBind {
    @Produces
    public QRCodeDetector providerQRCodeDetector() {
        OpenCV.loadLocally();
        return new QRCodeDetector();
    }
}
