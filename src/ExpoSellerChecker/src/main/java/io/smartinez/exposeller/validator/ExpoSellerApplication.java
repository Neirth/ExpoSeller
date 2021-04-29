package io.smartinez.exposeller.checker;

import io.smartinez.exposeller.checker.service.ValidatorService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class ExpoSellerApplication extends Application {
    private ValidatorService mValidatorService;
    private Stage mStage;

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();

        try (SeContainer container = initializer.disableDiscovery().addPackages(ExpoSellerApplication.class).initialize()) {
            mStage = stage;
            mValidatorService = container.select(ValidatorService.class).get();

            stage.setTitle("ExpoSeller Validator");
            stage.setScene(new Scene(new StackPane(), 450, 450));
            stage.getScene().setFill(Color.valueOf("#FFFFFF"));

            startValidation();

            stage.show();
        }
    }

    private void startValidation() {
        Runnable successValidation = () -> mStage.getScene().setFill(Color.web("#FF0000"));
        Runnable errorValidation = () -> mStage.getScene().setFill(Color.web("#00FF00"));

        mValidatorService.startValidator(successValidation, errorValidation);
    }
}
