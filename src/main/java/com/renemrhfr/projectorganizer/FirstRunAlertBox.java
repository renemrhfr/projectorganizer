package com.renemrhfr.projectorganizer;

import com.renemrhfr.projectorganizer.types.Settings;
import com.renemrhfr.projectorganizer.types.Tag;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class FirstRunAlertBox {

    private FirstRunAlertBox() {

    }

    private static final String FOLDER = "ProjectOrganizer";
    private static final DataStore dataStore = DataStore.getInstance();
    private static final FileHandler fileHandler = FileHandler.getInstance();
    private static final Settings settings = Settings.getInstance();

    public static void display() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
    window.setTitle("Setup");
    window.setMinHeight(270);
    window.setMinWidth(875);
    window.setResizable(false);

    FlowPane mainPane = new FlowPane();
    mainPane.setPadding(new Insets(0,0,10,0));

    // First Row
    VBox welcomeBox = new VBox();
    welcomeBox.setAlignment(Pos.CENTER);
    welcomeBox.setPrefHeight(100.0);
    Label welcomeLabel = new Label("Welcome to ProjectOrganizer!");
    welcomeLabel.getStyleClass().add("welcomeLabel");
    Label descriptionLabel = new Label("Before we start, lets setup a few things...");
    Region region = new Region();
    region.setPrefHeight(20);
    Separator sep = new Separator(Orientation.HORIZONTAL);
    sep.setPrefWidth(875);
    welcomeBox.getChildren().addAll(welcomeLabel, descriptionLabel, region, sep);

    // Second Row
    VBox inputsVBox = new VBox();
    HBox userHBox = new HBox();
    userHBox.setAlignment(Pos.TOP_LEFT);
    userHBox.setPadding(new Insets(10,10,0,0));
    userHBox.setPrefWidth(875.0);
    userHBox.setPrefHeight(37);
    userHBox.setSpacing(20.0);
    Label userLabel = new Label("Username:");
    userLabel.setPrefWidth(92);
    TextField userTextField = new TextField();
    userTextField.setPromptText("Enter your Name");
    userTextField.setPrefWidth(230.0);
    userHBox.getChildren().addAll(userLabel, userTextField);
    HBox pathHBox = new HBox();
    pathHBox.setAlignment(Pos.CENTER_LEFT);
    pathHBox.setPadding(new Insets(10,0,0,0));
    pathHBox.setPrefWidth(875.0);
    pathHBox.setPrefHeight(37);
    pathHBox.setSpacing(20.0);
    Label pathLabel = new Label("Path to your Dropbox");
    TextField pathTextField = new TextField();
    pathTextField.setPromptText("Root-Folder, i.e. C:\\Drobox");
    pathTextField.setPrefWidth(415.0);
    Button launchFileChooser = new Button("...");
    launchFileChooser.setOnAction(e -> {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(window);
        if (file != null) {
            String dropboxFolder = file.getAbsolutePath();
            if (dropboxFolder.endsWith("Dropbox")) {
                pathTextField.setText(dropboxFolder);
            }
        }
    });
    pathHBox.getChildren().addAll(pathLabel, pathTextField, launchFileChooser);
    inputsVBox.getChildren().addAll(userHBox, pathHBox);

    // Third Row
    HBox saveHBox = new HBox();
    saveHBox.setPrefWidth(875.0);
    saveHBox.setPrefHeight(50.0);
    saveHBox.setAlignment(Pos.CENTER);
    Button saveButton = new Button("Save");
    saveButton.setOnAction(e -> {
        if (!"".equals(userTextField.getText() ) && !"".equals(pathTextField.getText())) {
            settings.setUserName(userTextField.getText());
            settings.setDropboxRoot(pathTextField.getText());
            settings.setContactsFilename(settings.getDropboxRoot() + File.separator + FOLDER + File.separator + settings.getContactsFilename());
            settings.setTagsFilename(settings.getDropboxRoot() + File.separator + FOLDER + File.separator + settings.getTagsFilename());
            settings.setTracksFilename(settings.getDropboxRoot() + File.separator + FOLDER + File.separator + settings.getTracksFilename());
            fileHandler.rewriteFile(settings.getSettingsFilename(), settings);
            fileHandler.initFiles(settings.getTracksFilename(), settings.getContactsFilename(), settings.getContactsFilename());
            createInitTags();
            window.close();
        }
    });
    saveHBox.getChildren().add(saveButton);
    mainPane.getChildren().addAll(welcomeBox, inputsVBox, saveHBox);
    // Set up scene
    Scene scene = new Scene(mainPane, 270 ,875);
    window.setMaxHeight(270);
    window.setScene(scene);
    window.showAndWait();
    }

    private static void createInitTags() {
        List<Tag> initTags = MockData.mockTags();
        dataStore.setTags(initTags);
        fileHandler.rewriteFile(settings.getTagsFilename(), Tag.class, initTags);
    }

}
