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

public class ExpoSellerApplication extends Application {
    private Stage mStage;

    private final SeContainer mContainer;
    private final ValidatorService mValidatorService;
    private final ExecutorService mExecutorService;

    public ExpoSellerApplication() {
        mContainer = SeContainerInitializer.newInstance().addPackages(ExpoSellerApplication.class).initialize();
        mValidatorService = mContainer.select(ValidatorService.class).get();
        mExecutorService = mContainer.select(ExecutorService.class).get();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mStage = stage;

        stage.setTitle("ExpoSeller Validator");
        stage.setScene(new Scene(new StackPane(), 450, 450));
        stage.getScene().setFill(Color.valueOf("#FFFFFF"));
        stage.setResizable(false);

        startValidation();

        stage.setOnCloseRequest(we -> {
            try {
                ExpoSellerApplication.this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mExecutorService.shutdown();
        mContainer.close();
        System.exit(0);
    }

    private void startValidation() {
        Runnable successValidation = () -> mStage.getScene().setFill(Color.web("#00FF00"));
        Runnable errorValidation = () -> mStage.getScene().setFill(Color.web("#FF0000"));
        Runnable resetValidation = () -> mStage.getScene().setFill(Color.web("#FFFFFF"));

        mValidatorService.startValidator(successValidation, errorValidation, resetValidation);
    }
}
