package com.locuzzed.projectorganizer.controllers;

import com.locuzzed.projectorganizer.*;
import com.locuzzed.projectorganizer.types.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

public class EditTrackController {

    @FXML
    TextField modalTitleInput;

    @FXML
    TextField modalUUIDInput;

    @FXML
    ChoiceBox modalContactChoiceBox;

    @FXML
    ChoiceBox modalVersionChoiceBox;

    @FXML
    ListView modalSendoutListView;

    @FXML
    TextField modalProjectLocationInput;

    @FXML
    TextField modalPreviewLocationInput;

    @FXML
    TextField modalFilePathInput;

    @FXML
    ListView modalVersionsListView;

    @FXML
    CheckBox modalTag01CheckBox;

    @FXML
    CheckBox modalTag02CheckBox;
    @FXML
    CheckBox modalTag03CheckBox;

    @FXML
    CheckBox modalTag04CheckBox;

    @FXML
    CheckBox modalTag05CheckBox;

    @FXML
    ChoiceBox modalStageChoiceBox;

    @FXML
    Button modalSaveButton;

    @FXML
    Button modalCancelButton;

    @FXML
    Button modalSendoutButton;

    @FXML
    Button launchProjectsSelectorButton;

    @FXML
    Button launchPreviewsSelectorButton;

    @FXML
    Button modalRemoveSendoutButton;

    @FXML
    Button modalRemoveVersionButton;

    @FXML
    Button modalChangeVersionPathButton;

    @FXML
    HBox versionPathHBox;

    List<Integer> sendoutIndicesToRemove = new ArrayList<>();
    List<Sendout> sendoutsToAdd = new ArrayList<>();
    List<Integer> versionIndicesToRemove = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public static DataStore dataStore = DataStore.getInstance();

    public void initHandlers(Track track) {
        sendoutIndicesToRemove.clear();
        sendoutsToAdd.clear();
        versionIndicesToRemove.clear();

        modalSendoutListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            modalRemoveSendoutButton.setDisable(false);
        });

        modalRemoveSendoutButton.setOnMouseClicked(e -> {
            sendoutIndicesToRemove.add(modalSendoutListView.getSelectionModel().getSelectedIndex());
            modalSendoutListView.getItems().remove(modalSendoutListView.getSelectionModel().getSelectedIndex());
            modalSendoutListView.getSelectionModel().clearSelection();
        });
    }

    @FXML
    public void fillUI(Track track) {
        initHandlers(track);
        // Title, UUID, Project - and Previewlocation
        modalTitleInput.setText(track.getName());
        modalUUIDInput.setText(track.getUuid().toString());
        modalProjectLocationInput.setText(track.getProjectFolder() != null ? DropboxPathHelper.convertPath(track.getProjectFolder()) : "");
        modalPreviewLocationInput.setText(track.getPreviewFolder() != null ? DropboxPathHelper.convertPath(track.getPreviewFolder()) : "");
        //Versioning
        if (track.getFileHistory() != null) {
            modalVersionsListView.getItems().addAll(
                    track.getFileHistory()
                            .stream()
                            .sorted(Collections.reverseOrder())
                            .map(tv -> tv.getVersionDate()
                                    .format(formatter) + " | Version " + tv.getVersion() + " | " + DropboxPathHelper.convertPath(tv.getPath()))
                            .toList());
        }
        modalVersionsListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            modalChangeVersionPathButton.setDisable(false);
            if (track.getFileHistory() != null && track.getFileHistory().size() > 1 && modalVersionsListView.getSelectionModel().getSelectedIndex() != modalVersionsListView.getItems().size() -1) {
                modalRemoveVersionButton.setDisable(false);
            }
            if(modalVersionsListView.getSelectionModel().getSelectedIndex() == modalVersionsListView.getItems().size() -1) {
                modalRemoveVersionButton.setDisable(true);
            }
        });
        modalRemoveVersionButton.setOnMouseClicked(e -> {
            modalChangeVersionPathButton.setDisable(true);
            modalRemoveVersionButton.setDisable(true);
            if (track.getFileHistory() != null && track.getFileHistory().size() > 1) {
                versionIndicesToRemove.add(modalVersionsListView.getItems().size() - 1 - modalVersionsListView.getSelectionModel().getSelectedIndex());
                modalVersionsListView.getItems().remove(modalVersionsListView.getSelectionModel().getSelectedIndex());
                modalVersionsListView.getSelectionModel().clearSelection();
            }
        });
        // Sendout
        modalContactChoiceBox.getItems().addAll(dataStore.getContacts()
                .stream()
                .map(Contact::getName)
                .sorted()
                .toList());
        if (modalContactChoiceBox.getItems().size() > 0) {
            modalContactChoiceBox.setValue(modalContactChoiceBox.getItems().get(0));
        }
        if (!track.getFileHistory().isEmpty()) {
            modalVersionChoiceBox.getItems().addAll(track.getFileHistory()
                    .stream()
                    .map(TrackVersion::getVersion)
                    .sorted(Collections.reverseOrder())
                    .toList());
            modalVersionChoiceBox.setValue(modalVersionChoiceBox.getItems().get(0));
        }
        if (!track.getSentTo().isEmpty()) {
            modalSendoutListView.getItems().addAll(track.getSentTo()
                    .stream()
                    .sorted(Comparator.comparing(track2 -> track2.getRecipent().getName()))
                    .map(sendout -> sendout.getRecipent().getName() + ": v" + sendout.getVersionSent().getVersion() + " (" + sendout.getDateSent() + ")")
                    .toList());
        }
        // Tags
        List<Tag> tagList = dataStore.getTags();
        if(!tagList.isEmpty()) {
            modalTag01CheckBox.setText(tagList.get(0).getTagName());
            modalTag01CheckBox.setVisible(true);
            if (track.getTags().stream().anyMatch(e -> e.getTagName().equals(tagList.get(0).getTagName()))) {
                modalTag01CheckBox.setSelected(true);
            }
            if(tagList.size() > 1) {
                modalTag02CheckBox.setText(tagList.get(1).getTagName());
                modalTag02CheckBox.setVisible(true);
                if (track.getTags().stream().anyMatch(e -> e.getTagName().equals(tagList.get(1).getTagName()))) {
                    modalTag02CheckBox.setSelected(true);
                }
            }
            if(tagList.size() > 2) {
                modalTag03CheckBox.setText(tagList.get(2).getTagName());
                modalTag03CheckBox.setVisible(true);
                if (track.getTags().stream().anyMatch(e -> e.getTagName().equals(tagList.get(2).getTagName()))) {
                    modalTag03CheckBox.setSelected(true);
                }
            }
            if(tagList.size() > 3) {
                modalTag04CheckBox.setText(tagList.get(3).getTagName());
                modalTag04CheckBox.setVisible(true);
                if (track.getTags().stream().anyMatch(e -> e.getTagName().equals(tagList.get(3).getTagName()))) {
                    modalTag04CheckBox.setSelected(true);
                }
            }
            if(tagList.size() > 4) {
                modalTag05CheckBox.setText(tagList.get(4).getTagName());
                modalTag05CheckBox.setVisible(true);
                if (track.getTags().stream().anyMatch(e -> e.getTagName().equals(tagList.get(4).getTagName()))) {
                    modalTag05CheckBox.setSelected(true);
                }
            }
        }
        // Stage
        modalStageChoiceBox.getItems().addAll(TrackStage.values());
        modalStageChoiceBox.setValue(track.getStage());
        // Button
        modalSendoutButton.setOnAction(e -> {
            addSendout(track, modalContactChoiceBox.getValue().toString(),modalVersionChoiceBox.getValue() != null ? modalVersionChoiceBox.getValue().toString() : null);
        });
    }

    public void addSendout(Track track, String recipent, String version) {
        if (recipent == null || version == null) {
            return;
        }
        Optional<Contact> recipentContact = dataStore.getContacts().stream().filter(e -> e.getName().equals(recipent)).findFirst();
        Optional<TrackVersion> trackVersion = track.getFileHistory().stream().filter(e -> e.getVersion() == parseInt(version)).findFirst();
        Sendout sendout = new Sendout(recipentContact.get(), LocalDate.now(), trackVersion.get());
        modalSendoutListView.getItems().add(recipentContact.get().getName() + ": v" + version + " (" + LocalDate.now().format(formatter) + ")");
        sendoutsToAdd.add(sendout);
    }

    public void save(Track track) {
        track.setName(modalTitleInput.getText());
        track.setProjectFolder(modalProjectLocationInput.getText());
        track.setPreviewFolder(modalPreviewLocationInput.getText());
        track.getTags().forEach(tag -> tag = null);
        ArrayList<Tag> selectedTags = new ArrayList<>();
        ArrayList<Tag> tagList = dataStore.getTags();
        if (modalTag01CheckBox.isSelected()) {
            selectedTags.add(tagList.get(0));
        }
        if (modalTag02CheckBox.isSelected()) {
            selectedTags.add(tagList.get(1));
        }
        if (modalTag03CheckBox.isSelected()) {
            selectedTags.add(tagList.get(2));
        }
        if (modalTag04CheckBox.isSelected()) {
            selectedTags.add(tagList.get(3));
        }
        if (modalTag05CheckBox.isSelected()) {
            selectedTags.add(tagList.get(4));
        }
        track.setTags(selectedTags);
        track.setStage(TrackStage.valueOf(modalStageChoiceBox.getValue().toString()));
        for (Integer index : sendoutIndicesToRemove) {
            track.getSentTo().remove(index.intValue());
        }
        ArrayList<Sendout> sent = track.getSentTo();
        for(Sendout sendout : sendoutsToAdd) {
            sent.add(sendout);
        }
        for(Integer index : versionIndicesToRemove) {
            if(track.getCurrentVersion() == track.getFileHistory().get(index).getVersion()) {
                track.setCurrentVersion(track.getFileHistory().get(index-1).getVersion());
            }
            track.getFileHistory().remove(index.intValue());
        }
        // Dont need to change DataStore, since track is a reference to Datastore ArrayList
        FileHandler fileHandler = FileHandler.getInstance();
        Settings settings = Settings.getInstance();
        fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
    }
}
