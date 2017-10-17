package com.senorpez.projectcars.packetcapture;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.file.StandardOpenOption.*;
import static javafx.beans.binding.Bindings.not;

public class PacketCaptureController {
    private final SimpleObjectProperty<Optional<File>> outputFile = new SimpleObjectProperty<>(Optional.empty());
    private final SimpleBooleanProperty capturing = new SimpleBooleanProperty(false);

    private final int queueCapacity = 10000;
    private final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(queueCapacity);
    private final SimpleIntegerProperty queueSize = new SimpleIntegerProperty(0);

    private PacketReaderService packetReaderService;
    private PacketWriterService packetWriterService;

    @FXML
    private VBox root;

    @FXML
    private TextField textFieldOutputFile;

    @FXML
    private Button buttonOutputFile;

    @FXML
    private Button buttonBeginCapture;

    @FXML
    private Button buttonEndCapture;

    @FXML
    private ProgressBar queueStatus;

    @FXML
    private void initialize() throws IOException {
        textFieldOutputFile.textProperty().bind(
                Bindings.createStringBinding(
                        () -> outputFile.get().map(File::getAbsolutePath).orElse(""),
                        outputFile
                )
        );

        buttonOutputFile.disableProperty().bind(capturing);

        buttonBeginCapture.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !outputFile.get().isPresent() || capturing.get(),
                        outputFile,
                        capturing
                )
        );

        buttonEndCapture.disableProperty().bind(not(capturing));

        packetReaderService = new PacketReaderService(queue, queueSize);

        packetWriterService = new PacketWriterService(queue, queueSize);

        queueStatus.progressProperty().bind(
                Bindings.createDoubleBinding(
                        () -> (double) queueSize.get() / (double) queueCapacity,
                        queueSize));
    }

    @FXML
    void menuExit() {
        capturing.set(false);
        packetReaderService.cancel();
        packetWriterService.cancel();

        final Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void menuAbout() {
        final Alert about = new Alert(Alert.AlertType.INFORMATION);
        final String headerText = ApplicationInfo.getImplementationTitle() == null ?
                "Packet Capture" : ApplicationInfo.getImplementationTitle();
        final String contentText = ApplicationInfo.getImplementationVersion() == null ?
                "Development" : ApplicationInfo.getImplementationVersion();

        about.setTitle("About");
        about.setHeaderText(headerText);
        about.setContentText("Version: " + contentText);
        about.showAndWait();
    }

    @FXML
    private void buttonOutputFile() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PCARS", "*.pcars"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        outputFile.set(validateOutputFile(fileChooser.showSaveDialog(root.getScene().getWindow())));
    }

    @FXML
    private void buttonBeginCapture() throws IOException, InterruptedException {
        capturing.set(true);

        packetReaderService.start();

        final Writer writer = new SimplePCAPNGWriter(
                Files.newOutputStream(
                        outputFile.get().map(File::toPath).orElseThrow(IOException::new),
                        CREATE, TRUNCATE_EXISTING, WRITE));
        packetWriterService.start(writer);
    }

    @FXML
    private void buttonEndCapture() {
        capturing.set(false);

        packetWriterService.cancel();
        packetReaderService.cancel();

        packetWriterService.reset();
        packetReaderService.reset();
    }

    private static Optional<File> validateOutputFile(final File selectedFile) {
        return Optional.ofNullable(selectedFile).flatMap(file -> {
            if (file.exists()) {
                if (file.canWrite()) {
                    final Optional<ButtonType> result = showOverwriteAlert();
                    return Optional.ofNullable(result.filter(buttonType -> buttonType == ButtonType.OK).map(buttonType -> file).orElse(null));
                } else {
                    showWritePermissionAlert();
                    return Optional.empty();
                }
            } else {
                final File directory = file.getParentFile();
                if (directory.canWrite()) {
                    return Optional.of(file);
                } else {
                    showWritePermissionAlert();
                    return Optional.empty();
                }
            }
        });
    }

    private static Optional<ButtonType> showOverwriteAlert() {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention");
        alert.setHeaderText("File Exists");
        alert.setContentText("File currently exists. Proceeding with capture will destroy all existing data");
        return alert.showAndWait();
    }

    private static void showWritePermissionAlert() {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File Not Writable");
        alert.setContentText("File can not be written. Please check permissions or choose a different output.");
        alert.showAndWait();
    }
}
