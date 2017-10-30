package com.senorpez.projectcars.packetcapture;

import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testfx.api.FxAssert.verifyThat;

public class PacketCaptureGUITest extends ApplicationTest {
    @Override
    public void start(final Stage stage) throws Exception {
        new PacketCaptureGUI().start(stage);
    }

    @Test
    public void testInitialState() throws Exception {
        verifyThat("#textFieldOutputFile", (TextField node) -> node.getText().isEmpty());
        verifyThat("#textFieldOutputFile", (TextField node) -> !node.isEditable());
        verifyThat("#buttonOutputFile", (Button button) -> !button.isDisabled());
        verifyThat("#buttonBeginCapture", Button::isDisabled);
        verifyThat("#buttonEndCapture", Button::isDisabled);
    }

    @Test
    public void testMenuBarItems() throws Exception {
        clickOn("File");
        clickOn("Help");
    }

    @Test
    public void testAboutPopup() throws Exception {
        clickOn("Help");
        clickOn("About");

        final DialogPane aboutPane = (DialogPane) lookup("Version: Development").query().getParent();
        final String windowTitle = ((Stage) aboutPane.getScene().getWindow()).getTitle();
        final String headingText = ((Label) ((GridPane) aboutPane.getChildren().get(0)).getChildren().get(0)).getText();
        final String contentText = ((Label) aboutPane.getChildren().get(1)).getText();

        assertThat(windowTitle, is("About"));
        assertThat(headingText, is("Packet Capture"));
        assertThat(contentText, is("Version: Development"));

        clickOn("OK");
    }
}
