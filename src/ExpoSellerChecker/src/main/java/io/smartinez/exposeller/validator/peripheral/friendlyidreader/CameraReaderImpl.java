package io.smartinez.exposeller.validator.peripheral.friendlyidreader;

import io.smartinez.exposeller.validator.listener.OnScannedCodeListener;
import jakarta.enterprise.context.ApplicationScoped;
import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.videoio.VideoCapture;

import jakarta.inject.Inject;

import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@SuppressWarnings({"unused", "UnusedAssignment"})
public class CameraReaderImpl implements IFriendlyIdReader {
    private final Logger mLogger;
    private final QRCodeDetector mQrCodeDetector;
    private final ExecutorService mExecutorService;

    @Inject
    public CameraReaderImpl(QRCodeDetector qrCodeDetector, ExecutorService executorService, Logger logger) {
        this.mQrCodeDetector = qrCodeDetector;
        this.mExecutorService = executorService;
        this.mLogger = logger;
    }

    @Override
    public void detectFriendlyId(OnScannedCodeListener onScannedCodeListener) {
        mExecutorService.execute(() -> {
            Mat imageArray = new Mat();

            VideoCapture videoDevice = new VideoCapture();
            videoDevice.open(0);

            String qrCodeDetected;
            String prevQrCodeDetected = "";

            while (true) {
                if (videoDevice.isOpened()) {
                    try {
                        videoDevice.read(imageArray);
                        qrCodeDetected = mQrCodeDetector.detectAndDecode(imageArray);

                        if (qrCodeDetected != null && !qrCodeDetected.isEmpty() && !prevQrCodeDetected.equals(qrCodeDetected)) {
                            mLogger.log(Level.FINE, "Detected FriendlyId from QR Code Camera: " + qrCodeDetected);
                            onScannedCodeListener.onScannedCodeListener(qrCodeDetected);

                            prevQrCodeDetected = qrCodeDetected;
                        }

                        qrCodeDetected = null;
                    } catch (Exception ignored) { }
                } else {
                    break;
                }
            }
        });
    }
}
