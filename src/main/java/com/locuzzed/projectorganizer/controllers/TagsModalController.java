package com.locuzzed.projectorganizer.controllers;

import com.locuzzed.projectorganizer.DataStore;
import com.locuzzed.projectorganizer.FileHandler;
import com.locuzzed.projectorganizer.types.Settings;
import com.locuzzed.projectorganizer.types.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;

public class TagsModalController {

    @FXML
    TextField tagsModalTag01Name;

    @FXML
    TextField tagsModalTag02Name;

    @FXML
    TextField tagsModalTag03Name;

    @FXML
    TextField tagsModalTag04Name;

    @FXML
    TextField tagsModalTag05Name;

    @FXML
    ColorPicker tagsModalTag01Color;

    @FXML
    ColorPicker tagsModalTag02Color;

    @FXML
    ColorPicker tagsModalTag03Color;

    @FXML
    ColorPicker tagsModalTag04Color;

    @FXML
    ColorPicker tagsModalTag05Color;

    @FXML
    Button tagsModalSave;

    public void initialize() {
        DataStore dataStore = DataStore.getInstance();

        tagsModalTag01Color.setValue(dataStore.getTags().get(0).getTagColor());
        tagsModalTag01Name.setText(dataStore.getTags().get(0).getTagName());

        tagsModalTag02Color.setValue(dataStore.getTags().get(1).getTagColor());
        tagsModalTag02Name.setText(dataStore.getTags().get(1).getTagName());

        tagsModalTag03Color.setValue(dataStore.getTags().get(2).getTagColor());
        tagsModalTag03Name.setText(dataStore.getTags().get(2).getTagName());

        tagsModalTag04Color.setValue(dataStore.getTags().get(3).getTagColor());
        tagsModalTag04Name.setText(dataStore.getTags().get(3).getTagName());

        tagsModalTag05Color.setValue(dataStore.getTags().get(4).getTagColor());
        tagsModalTag05Name.setText(dataStore.getTags().get(4).getTagName());
    }

    public void updateTags() {
        DataStore dataStore = DataStore.getInstance();
        FileHandler fh = FileHandler.getInstance();
        Settings settings = Settings.getInstance();

        dataStore.getTags().get(0).setTagColor(tagsModalTag01Color.getValue());
        dataStore.getTags().get(0).setTagName(tagsModalTag01Name.getText());

        dataStore.getTags().get(1).setTagColor(tagsModalTag02Color.getValue());
        dataStore.getTags().get(1).setTagName(tagsModalTag02Name.getText());

        dataStore.getTags().get(2).setTagColor(tagsModalTag03Color.getValue());
        dataStore.getTags().get(2).setTagName(tagsModalTag03Name.getText());

        dataStore.getTags().get(3).setTagColor(tagsModalTag04Color.getValue());
        dataStore.getTags().get(3).setTagName(tagsModalTag04Name.getText());

        dataStore.getTags().get(4).setTagColor(tagsModalTag05Color.getValue());
        dataStore.getTags().get(4).setTagName(tagsModalTag05Name.getText());

        fh.rewriteFile(settings.getTagsFilename(), Tag.class, dataStore.getTags());

    }
}
