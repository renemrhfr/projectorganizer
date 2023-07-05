package com.locuzzed.projectorganizer;

import com.locuzzed.projectorganizer.controllers.ApplicationController;
import com.locuzzed.projectorganizer.types.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private static DataStore dataStore = DataStore.getInstance();
    private static boolean firstRun;

    public static void main(String[] args) {
        firstRun = checkForFirstRun();
        if (!firstRun) {
            readFiles();
        }
        launch();
    }

    public static boolean checkForFirstRun() {
        Settings settings  = Settings.getInstance();
        File settingsFile = new File(settings.getSettingsFilename());
        return !settingsFile.exists();
    }

    @Override
    public void start(Stage stage) throws IOException {
        if (firstRun) {
            FirstRunAlertBox.display();
        }
        FXMLLoader applicationLoader = new FXMLLoader(Main.class.getResource("application-view.fxml"));
        Parent mainRoot = applicationLoader.load();
        Scene mainScene = new Scene(mainRoot, 820, 470);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        stage.setMinWidth(820);
        stage.setResizable(true);
        stage.setTitle("Project Organizer");
        stage.setScene(mainScene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {

                Platform.exit();
                System.exit(0);
            }
        });



        ApplicationController mainController = (ApplicationController) applicationLoader.getController();
        mainScene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != mainScene
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        mainScene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    handleNewFile(db.getFiles());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                mainController.refreshUI();
                mainController.tabPane.getSelectionModel().select(2);
            }
        });

        mainController.sortChoiceBox.setOnAction(e -> {
            switch(mainController.sortChoiceBox.getValue().toString()) {
                case "Createate":
                    dataStore.getTracks().sort(Comparator.comparing(track -> track.getFileHistory()
                            .get(0)
                            .getVersionDate()));
                    mainController.refreshUI();
                    break;
                case "Last Update":
                    dataStore.getTracks()
                            .sort((track1, track2) -> track2.getFileHistory().get(track2.getFileHistory().size()-1).getVersionDate()
                                    .compareTo(
                                    track1.getFileHistory().get(track1.getFileHistory().size()-1).getVersionDate()));
                    mainController.refreshUI();
                    break;
                case "Rating":
                    dataStore.getTracks().sort(Comparator.comparing(Track::getRating).reversed());
                    mainController.refreshUI();
                    break;
                case "Name":
                default:
                    dataStore.getTracks().sort(Comparator.comparing(Track::getName));
                    mainController.refreshUI();
            }
        });

        mainController.refreshUI();
    }

    public static void handleNewFile(List<File> files) {
        DataStore tempstore = DataStore.getInstance();
        for (File firstFile : files) {
            String filePath = firstFile.getPath();
            String previewFolder = firstFile.getParent();
            String name = firstFile.getName().substring(0, firstFile.getName().length() - 4).replaceFirst("v[0-9]+", "");;

            TrackStage stage = TrackStage.DRAFT;
            Track addTrack = new Track(filePath, previewFolder, name, null, null, stage,
                    null, null, false, false, null, 1, new ArrayList<>(), 1, new ArrayList<>(), false);
            TrackVersion tv = new TrackVersion(1, LocalDate.now(), filePath, false, addTrack.getUuid());
            addTrack.getFileHistory().add(tv);
            tempstore.getTracks().add(addTrack);
        }
        FileHandler fH = FileHandler.getInstance();
        Settings se = Settings.getInstance();
        fH.rewriteFile(se.getTracksFilename(), Track.class, tempstore.getTracks());
    }

    public static void readFiles() {
        FileHandler fileHandler = FileHandler.getInstance();
        Settings settings  = Settings.getInstance();
        File settingsFile = new File(settings.getSettingsFilename());
        if (settingsFile.exists()) {
            ArrayList<Settings> savedSettings = fileHandler.readFile(settings.getSettingsFilename(), Settings.class);
            Settings loadedSettings = savedSettings.get(0);
            settings.setUserName(loadedSettings.getUserName());
            settings.setDropboxRoot(loadedSettings.getDropboxRoot());
            settings.setSettingsFilename(loadedSettings.getSettingsFilename());
            settings.setContactsFilename(loadedSettings.getContactsFilename());
            settings.setTagsFilename(loadedSettings.getTagsFilename());
            settings.setTracksFilename(loadedSettings.getTracksFilename());
        }
        File contacts = new File(settings.getContactsFilename());
        if (contacts.exists()) {
            ArrayList<Contact> savedContacts = fileHandler.readFile(settings.getContactsFilename(), Contact.class);
            if (savedContacts != null) {
                dataStore.getContacts().addAll(savedContacts);
            }
        }
        File tags = new File(settings.getTagsFilename());
        if (tags.exists()) {
            ArrayList<Tag> savedTags = fileHandler.readFile(settings.getTagsFilename(), Tag.class);
            savedTags.forEach(tag -> tag.setTagColor(tag.getTagColorSerializable().getFXColor()));
            dataStore.getTags().addAll(savedTags);
        }
        File tracks = new File(settings.getTracksFilename());
        if (tracks.exists()) {
            ArrayList<Track> savedTracks = fileHandler.readFile(settings.getTracksFilename(), Track.class);
            if (savedTracks != null) {
                savedTracks.forEach(track -> track.getTags().forEach(tag -> tag.setTagColor(tag.getTagColorSerializable().getFXColor())));
                dataStore.getTracks().addAll(savedTracks);
            }
        }
    }
}