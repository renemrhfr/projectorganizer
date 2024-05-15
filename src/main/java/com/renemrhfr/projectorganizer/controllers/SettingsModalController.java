package com.renemrhfr.projectorganizer.controllers;

import com.renemrhfr.projectorganizer.types.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsModalController {

    @FXML
    TextField nameTextField;

    @FXML
    TextField folderTextField;

    @FXML
    Button fileChooserButton;

    @FXML
    Button saveButton;

    @FXML
    TextField trackPathTextLabel;

    @FXML
    TextField contactPathTextLabel;

    @FXML
    TextField tagPathTextLabel;

    public void initialize() {
        Settings settings = Settings.getInstance();
        nameTextField.setText(settings.getUserName());
        folderTextField.setText(settings.getDropboxRoot());

        trackPathTextLabel.setText(settings.getTracksFilename());
        contactPathTextLabel.setText(settings.getContactsFilename());
        tagPathTextLabel.setText(settings.getTagsFilename());
    }

}
