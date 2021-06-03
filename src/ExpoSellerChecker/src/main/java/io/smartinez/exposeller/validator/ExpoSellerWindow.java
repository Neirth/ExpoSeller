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
package io.smartinez.exposeller.validator;

import io.smartinez.exposeller.validator.service.ValidatorService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.concurrent.ExecutorService;


public class ExpoSellerWindow extends Application {
    // Prepare the canvas
    private Stage mStage;

    // Prepare the dependency injection container
    private final SeContainer mContainer;

    // Instance of validator use case
    private final ValidatorService mValidatorService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    public ExpoSellerWindow() {
        mContainer = SeContainerInitializer.newInstance().addPackages(ExpoSellerApplication.class).initialize();
        mValidatorService = mContainer.select(ValidatorService.class).get();
        mExecutorService = mContainer.select(ExecutorService.class).get();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Bind the canvas
        mStage = stage;

        // Configure the canvas
        stage.setTitle("ExpoSeller Validator");
        stage.setScene(new Scene(new StackPane(), 450, 450));
        stage.getScene().setFill(Color.valueOf("#FFFFFF"));
        stage.setResizable(false);

        // Start a validation
        startValidation();

        // Set callback to close the canvas
        stage.setOnCloseRequest(we -> {
            try {
                ExpoSellerWindow.this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Show the canvas
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Call to parent method
        super.stop();

        // Shutdown the background threads
        mExecutorService.shutdown();

        // Close the dependency injection
        mContainer.close();

        // Exit the application
        System.exit(0);
    }

    private void startValidation() {
        // Prepare callbacks
        Runnable successValidation = () -> mStage.getScene().setFill(Color.web("#00FF00"));
        Runnable errorValidation = () -> mStage.getScene().setFill(Color.web("#FF0000"));
        Runnable resetValidation = () -> mStage.getScene().setFill(Color.web("#FFFFFF"));

        // Initialize the validation
        mValidatorService.startValidator(successValidation, errorValidation, resetValidation);
    }
}
