package com.senorpez.projectcars.packetcapture;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketCaptureGUI extends Application {
    private PacketCaptureController controller;
    private final static Logger logger = LoggerFactory.getLogger(PacketCaptureGUI.class);

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("packetcapture.fxml"));
        final Parent root = loader.load();
        controller = loader.getController();
        final Scene scene = new Scene(root);

        stage.setTitle("Project CARS UDP Packet Capture");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Shutting Down");
        controller.menuExit();
    }

    public static void main(final String[] args) throws UnsupportedOperationException {
        logger.info("Launching");
        launch(args);
    }
}