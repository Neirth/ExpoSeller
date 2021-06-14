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
package io.neirth.exposeller.validator.peripheral.friendlyidreader;

import io.neirth.exposeller.validator.listener.OnScannedCodeListener;
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
    // Instance of logger
    private final Logger mLogger;

    // Instance of QR Code Detector
    private final QRCodeDetector mQrCodeDetector;

    // Instance of Executor Service
    private final ExecutorService mExecutorService;

    @Inject
    public CameraReaderImpl(QRCodeDetector qrCodeDetector, ExecutorService executorService, Logger logger) {
        this.mQrCodeDetector = qrCodeDetector;
        this.mExecutorService = executorService;
        this.mLogger = logger;
    }

    /**
     * Method for scan a friendly id
     *
     * @param onScannedCodeListener Callback to detect the scanned code
     */
    @Override
    public void detectFriendlyId(OnScannedCodeListener onScannedCodeListener) {
        mExecutorService.execute(() -> {
            // Prepare variable of image array
            Mat imageArray = new Mat();

            // Instance a video capture
            VideoCapture videoDevice = new VideoCapture();

            // Open the camera
            videoDevice.open(0);

            // Prepare the variables
            String qrCodeDetected = "";
            String prevQrCodeDetected = "";

            while (true) {
                // Check if the video device is open
                if (videoDevice.isOpened()) {
                    try {
                        // Read a video frame and save it into imageArray
                        videoDevice.read(imageArray);

                        // Detect the QR code and save it into string
                        qrCodeDetected = mQrCodeDetector.detectAndDecode(imageArray);

                        // Detect if not is the same has a previous code
                        if (qrCodeDetected != null && !qrCodeDetected.isEmpty() && !prevQrCodeDetected.equals(qrCodeDetected)) {
                            // Inform in log
                            mLogger.log(Level.INFO, "Detected FriendlyId from QR Code Camera: " + qrCodeDetected);

                            // Execute callback
                            onScannedCodeListener.onScannedCodeListener(qrCodeDetected);

                            // Set the qrCode has previous
                            prevQrCodeDetected = qrCodeDetected;
                        }

                        // Clean the qr code
                        qrCodeDetected = null;
                    } catch (Exception ignored) { }
                } else {
                    break;
                }
            }
        });
    }
}
