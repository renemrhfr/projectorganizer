package com.renemrhfr.projectorganizer.controllers;

import com.renemrhfr.projectorganizer.DataStore;
import com.renemrhfr.projectorganizer.FileHandler;
import com.renemrhfr.projectorganizer.Main;
import com.renemrhfr.projectorganizer.types.Settings;
import com.renemrhfr.projectorganizer.types.Track;
import com.renemrhfr.projectorganizer.types.TrackVersion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * This helper-class is in charge of creating the Modals of this Application.
 * The individual Modals are named as [...]Controller classes
 */
public class CreateModalHelper {

    public static CreateModalHelper instance;
    public static FileHandler fileHandler = FileHandler.getInstance();
    public static Settings settings = Settings.getInstance();
    public static DataStore dataStore = DataStore.getInstance();

    public static CreateModalHelper getInstance() {
        if (instance == null) {
            instance = new CreateModalHelper();
        }
        return instance;
    }

    public void createEditModal(ApplicationController controller, Track track) {
        FXMLLoader modalLoader = new FXMLLoader(Main.class.getResource("editTrack-modal.fxml"));
        Parent modalRoot = null;
        try {
            modalRoot = modalLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene modalScene = new Scene(modalRoot, 485, 558);
        Stage modal = new Stage();
        modal.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        modal.setResizable(false);
        modal.setScene(modalScene);
        EditTrackController modalController = (EditTrackController) modalLoader.getController();
        modalController.fillUI(track);
        modal.show();

        modalController.modalCancelButton.setOnAction(e -> modal.close());
        modalController.modalSaveButton.setOnAction(e -> {
            modalController.save(track);
            controller.refreshUI();
        });

        modalController.launchProjectsSelectorButton.setOnAction(e -> {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(modal);
                if (file != null) {
                    String dropboxFolder = file.getAbsolutePath();
                        modalController.modalProjectLocationInput.setText(dropboxFolder);
                }
        });

        modalController.modalChangeVersionPathButton.setOnMouseClicked(e -> {
            FileChooser directoryChooser = new FileChooser();
            File file = directoryChooser.showOpenDialog(modal);
            if (file != null && file.isFile()) {
                String dropboxFolder = file.getAbsolutePath();
                track.getFileHistory().get(modalController.modalVersionsListView.getItems().size() - 1 - modalController.modalVersionsListView.getSelectionModel().getSelectedIndex()).setPath(dropboxFolder);
                controller.refreshUI();
            }
        });

        modalController.launchPreviewsSelectorButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(modal);
            if (file != null) {
                String dropboxFolder = file.getAbsolutePath();
                modalController.modalPreviewLocationInput.setText(dropboxFolder);
            }
        });

    }

    public void createVersionModal(ApplicationController controller, Track track) {
        FXMLLoader modalLoader = new FXMLLoader(Main.class.getResource("newVersion-modal.fxml"));
        Parent modalRoot = null;
        try {
            modalRoot = modalLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene modalScene = new Scene(modalRoot, 502, 205);
        Stage modal = new Stage();
        modal.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        modal.setResizable(false);
        modal.setScene(modalScene);
        NewVersionModalController modalController = (NewVersionModalController) modalLoader.getController();
        modalController.fillUI(track);
        modal.show();
        modalController.newVersionModalSaveButton.setOnAction(e -> {
            track.setCurrentVersion(parseInt(modalController.newVersionModalVersionTextfield.getText()));
            TrackVersion tv = new TrackVersion(track.getCurrentVersion(), LocalDate.now(), modalController.newVersionModallPathTextfield.getText(), false, track.getUuid());
            track.getFileHistory().add(tv);
            fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
            controller.refreshUI();
            modal.close();
        });
        modalScene.setOnDragOver(event -> {
                if (event.getGestureSource() != modalScene
                        && event.getDragboard()
                        .hasFiles() && event.getDragboard()
                        .getFiles()
                        .size() == 1) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        );
        modalScene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                modalController.newVersionModalSaveButton.setDisable(false);
                modalController.newVersionModallPathTextfield.setText(file.getAbsolutePath());
            }
            event.setDropCompleted(success);
            event.consume();
        });
        modalController.newVersionModalVersionTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    modalController.newVersionModalVersionTextfield.setText(t1.replaceAll("[^\\d]", ""));
                } else if (track.getFileHistory().stream().anyMatch(tv -> tv.getVersion() == parseInt(t1))) {
                    modalController.newVersionModalVersionTextfield.setText(String.valueOf(track.getCurrentVersion() + 1));
                }
            }
        });
    }

    public void createTagsModal() {
        FXMLLoader modalLoader = new FXMLLoader(Main.class.getResource("TagsModal.fxml"));
        Parent modalRoot = null;
        try {
            modalRoot = modalLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene modalScene = new Scene(modalRoot, 502, 400);
        Stage modal = new Stage();
        modal.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        modal.setResizable(false);
        modal.setScene(modalScene);
        TagsModalController modalController = (TagsModalController) modalLoader.getController();
        modalController.tagsModalSave.setOnAction(e -> {
            modalController.updateTags();
            modal.close();
        });
        modal.show();
    }

    public void createContactModal() {
        FXMLLoader modalLoader = new FXMLLoader(Main.class.getResource("ContactsModal.fxml"));
        Parent modalRoot = null;
        try {
            modalRoot = modalLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene modalScene = new Scene(modalRoot, 500, 542.0);
        Stage modal = new Stage();
        modal.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        modal.setResizable(false);
        modal.setScene(modalScene);
        ContactModalController modalController = (ContactModalController) modalLoader.getController();
        modal.show();
        modalController.contactModalClose.setOnAction(e -> modal.close());
    }

    public void createSettingsModal() {
        FXMLLoader modalLoader = new FXMLLoader(Main.class.getResource("SettingsModal.fxml"));
        Parent modalRoot = null;
        try {
            modalRoot = modalLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene modalScene = new Scene(modalRoot, 525, 300);
        Stage modal = new Stage();
        modal.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        modal.setResizable(false);
        modal.setScene(modalScene);
        SettingsModalController modalController = (SettingsModalController) modalLoader.getController();
        modal.show();

        modalController.fileChooserButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(modal);
            if (file != null) {
                String dropboxFolder = file.getAbsolutePath();
                if (dropboxFolder.endsWith("Dropbox")) {
                    modalController.folderTextField.setText(dropboxFolder);
                }
            }
        });

        modalController.saveButton.setOnAction(e -> {
            if (!"".equals(modalController.nameTextField.getText() ) && !"".equals(modalController.folderTextField.getText())) {
                settings.setDropboxRoot(modalController.folderTextField.getText());
                settings.setUserName(modalController.nameTextField.getText());
                fileHandler.rewriteFile(settings.getSettingsFilename(), settings);
                modal.close();
            }
        });
    }

}
