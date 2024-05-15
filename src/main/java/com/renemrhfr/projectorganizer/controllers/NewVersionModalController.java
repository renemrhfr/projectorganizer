package com.renemrhfr.projectorganizer.controllers;

import com.renemrhfr.projectorganizer.types.Track;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NewVersionModalController {

    @FXML
    TextField newVersionModalTrackname;

    @FXML
    TextField newVersionModalUUIDTextfield;

    @FXML
    TextField newVersionModallPathTextfield;

    @FXML
    TextField newVersionModalVersionTextfield;

    @FXML
    Button newVersionModalSaveButton;

    public void fillUI(Track track) {
        newVersionModalTrackname.setText(track.getName());
        newVersionModalUUIDTextfield.setText(track.getUuid().toString());
        newVersionModalVersionTextfield.setText(track.getCurrentVersion()+ 1 + "");
    }

}
