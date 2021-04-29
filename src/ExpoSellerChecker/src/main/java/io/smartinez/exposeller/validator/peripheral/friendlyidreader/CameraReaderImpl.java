package io.smartinez.exposeller.checker.peripheral.friendlyidreader;

import io.smartinez.exposeller.checker.util.listener.OnScannedCodeListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.videoio.VideoCapture;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class CameraReaderImpl implements IFriendlyIdReader {
    private Logger mLogger;
    private QRCodeDetector mQrCodeDetector;
    private ExecutorService mExecutorService;

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
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            VideoCapture videoDevice = new VideoCapture();
            videoDevice.open(0);

            String qrCodeDetected;

            while (true) {
                if (videoDevice.isOpened()) {
                    videoDevice.read(imageArray);
                    qrCodeDetected = mQrCodeDetector.detectAndDecode(imageArray);

                    mLogger.log(Level.INFO, "Detected FriendlyId from QR Camera: " + qrCodeDetected);
                    onScannedCodeListener.onScannedCodeListener(qrCodeDetected);
                } else {
                    break;
                }
            }
        });
    }
}
