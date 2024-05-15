package com.renemrhfr.projectorganizer.controllers;

import com.renemrhfr.projectorganizer.*;
import com.renemrhfr.projectorganizer.types.Sendout;
import com.renemrhfr.projectorganizer.types.Settings;
import com.renemrhfr.projectorganizer.types.Track;
import com.renemrhfr.projectorganizer.types.TrackVersion;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class ApplicationController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private VBox workInProgressMainVBox;

    @FXML
    private VBox releaseMainVBox;

    @FXML
    private VBox draftsMainVBox;

    @FXML
    public TabPane tabPane;

    @FXML
    public CheckBox filterDone;

    @FXML
    public CheckBox filterTag01;

    @FXML
    public CheckBox filterTag02;

    @FXML
    public CheckBox filterTag03;

    @FXML
    public CheckBox filterTag04;

    @FXML
    public CheckBox filterTag05;

    @FXML
    public TextField filterName;

    @FXML
    public ChoiceBox sortChoiceBox;

    private MediaPlayer player;
    private Timer timer;
    private TimerTask task;

    private CreateModalHelper createModalHelper = CreateModalHelper.getInstance();
    private DataStore dataStore = DataStore.getInstance();
    private static FileHandler fileHandler = FileHandler.getInstance();
    private static Settings settings = Settings.getInstance();


    public void initialize() {
        sortChoiceBox.getItems().addAll("Name", "Latest Update", "Rating", "Createdate");
    }

    /**
     * This Method gets called everytime we need to refresh the UI (i.e. when User adds a new Track).
     */
    public void refreshUI() {
        List<Track> tracks = dataStore.getTracks();
        releaseMainVBox.getChildren().clear();
        workInProgressMainVBox.getChildren().clear();
        draftsMainVBox.getChildren().clear();

        filterTag01.setText(dataStore.getTags().get(0).getTagName());
        filterTag02.setText(dataStore.getTags().get(1).getTagName());
        filterTag03.setText(dataStore.getTags().get(2).getTagName());
        filterTag04.setText(dataStore.getTags().get(3).getTagName());
        filterTag05.setText(dataStore.getTags().get(4).getTagName());

        for(Track track : tracks) {
            switch (track.getStage()) {
                case SCHEDULED_FOR_RELEASE:
                    addTrackToReleaseSchedule(track);
                    break;
                case WORK_IN_PROGRESS:
                    addTrackToWIP(track);
                    break;
                case DRAFT:
                default:
                    addTrackToDrafts(track);
                    break;
            }
        }
    }

    /**
     * This Method checks if the track to be added is relevant for the set filters and creates the UI-Elements for it.
     * @param track: The Track to be added.
     */
    public void addTrackToReleaseSchedule(Track track) {
        if (track.isDone() && !ResultFilter.isIncludeDone()) {
            return;
        }
        boolean textCheck = ResultFilter.getTextFilter().equals("") || track.getName().contains(ResultFilter.getTextFilter());
        if(!textCheck) {
            return;
        }
        // First Row
        HBox firstRow = new HBox();
        firstRow.setPrefWidth(800);
        firstRow.getStyleClass().add("wip-firstrow");
        firstRow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        Label trackTitle = new Label(track.getName() + " (v" + track.getCurrentVersion() + ")");
        trackTitle.getStyleClass().add("tracktitle");
        trackTitle.setPrefWidth(300);
        Region regionBetweenTitleAndPlayer = new Region();
        regionBetweenTitleAndPlayer.setPrefHeight(18.0);
        regionBetweenTitleAndPlayer.setPrefWidth(247.0);
        HBox.setHgrow(regionBetweenTitleAndPlayer, Priority.NEVER);
        FontIcon playPauseButton = new FontIcon();
        playPauseButton.setIconLiteral("dashicons-controls-play");
        playPauseButton.setIconSize(20);
        ProgressBar progressBar = new ProgressBar();
        progressBar.getStyleClass().add("musicprogressbar");
        progressBar.setProgress(0);
        Optional<TrackVersion> latestVersionOptional = track.getFileHistory().stream().filter(version -> version.getVersion() == track.getCurrentVersion()).findFirst();
        HBox playerHBox = new HBox();
        playerHBox.getChildren().add(playPauseButton);
        if (latestVersionOptional.isPresent()) {
            TrackVersion latestVersion = latestVersionOptional.get();
            File latestVersionFile = new File(DropboxPathHelper.convertPath(latestVersion.getPath()));
            if (latestVersionFile.exists()) {
                playerHBox.getChildren().add(progressBar);
                playPauseButton.setOnMouseClicked(e -> {
                    if (playPauseButton.getIconLiteral()
                            .equals("dashicons-controls-pause")) {
                        playPauseButton.setIconLiteral("dashicons-controls-play");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), false, progressBar, playPauseButton);
                    } else {
                        playPauseButton.setIconLiteral("dashicons-controls-pause");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), true, progressBar, playPauseButton);
                    }
                });
            }
            else {
                playPauseButton.setIconLiteral("dashicons-dismiss");
                Label errorLabel = new Label("Previewfile not found! Please Edit and check the Filepath!");
                regionBetweenTitleAndPlayer.setPrefWidth(180);
                playerHBox.getChildren().add(errorLabel);
            }
        }
        firstRow.getChildren().addAll(trackTitle, regionBetweenTitleAndPlayer, playerHBox);

        // Second Row
        HBox secondRow = new HBox();
        secondRow.setPrefHeight(220);
        secondRow.setPrefWidth(800);
        secondRow.setSpacing(20);
        secondRow.setPadding(new Insets(5,5,5,0));
        secondRow.getStyleClass().add("wip-secondrow");
        VBox notesVBox = new VBox();
        Label notesLabel = new Label("Notes");
        VBox textAreaVBox = new VBox();
        TextArea notes = new TextArea(track.getNotes());
        Button updateButton = new Button("Save");
        notes.textProperty().addListener(change -> {
                track.setNotes(notes.getText());
                if(!textAreaVBox.getChildren().contains(updateButton)) {
                    updateButton.setVisible(false);
                    updateButton.setVisible(true);
                    textAreaVBox.getChildren().add(updateButton);
                    updateButton.setOnAction(e -> {
                        track.setNotes(notes.getText());
                        fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                        textAreaVBox.getChildren().remove(updateButton);
                    });
                }
            }
        );
        textAreaVBox.getChildren().add(notes);
        notes.setPrefHeight(220);
        notes.setPrefWidth(302);
        notes.setPromptText("Space for ToDos and Notes...");
        notes.getStyleClass().add("wip-notes");
        notesVBox.getChildren().addAll(notesLabel, textAreaVBox);
        VBox sentToVBox = new VBox();
        Label sentToLabel = new Label("Sent to:");
        ListView<String> sentToListView = new ListView<>();
        for(Sendout se : track.getSentTo()) {
            sentToListView.getItems().add(se.getRecipent().getName() + ": " + "v" + se.getVersionSent().getVersion() + " (" + se.getDateSent().format(formatter) + ")");
        }
        sentToListView.setPrefHeight(200);
        sentToListView.setMaxHeight(200);
        sentToListView.setPrefWidth(200);
        sentToListView.getStyleClass().add("wip-sendoutlist");
        sentToVBox.getChildren().addAll(sentToLabel, sentToListView);
        VBox ratingAndButtonBox = new VBox();
        ratingAndButtonBox.setPrefHeight(126);
        ratingAndButtonBox.setPrefWidth(232);
        ratingAndButtonBox.getStyleClass().add("wip-rating-and-button-box");
        ButtonBar topButtonBar = new ButtonBar();
        topButtonBar.setPrefHeight(40);
        topButtonBar.setPrefWidth(200);
        topButtonBar.setPadding(new Insets(0,50,5,5));
        Button projectsButton = new Button("Projects");
        projectsButton.getStyleClass().add("wip-projects-button");
        if (track.getProjectFolder() != null && !Objects.equals(track.getProjectFolder(), "")) {
            projectsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getProjectFolder())));
        }
        Button previewsButton = new Button("Previews");
        previewsButton.getStyleClass().add("wip-previews-button");
        if (track.getPreviewFolder() != null && !Objects.equals(track.getPreviewFolder(), "")) {
            previewsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getPreviewFolder())));
        }
        topButtonBar.getButtons().addAll(projectsButton, previewsButton);
        ButtonBar bottomButtonBar = new ButtonBar();
        bottomButtonBar.setPrefHeight(40);
        bottomButtonBar.setPrefWidth(200);
        bottomButtonBar.setLayoutX(10);
        bottomButtonBar.setLayoutY(66);
        bottomButtonBar.setPadding(new Insets(0,50,5,5));
        Button newVersionButton = new Button("Version++");
        newVersionButton.getStyleClass().add("wip-newversion-button");
        newVersionButton.setOnAction(e -> createModalHelper.createVersionModal(this, track));
        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("wip-edit-button");
        editButton.onActionProperty().setValue(e -> createModalHelper.createEditModal(this, track));
        bottomButtonBar.getButtons().addAll(newVersionButton, editButton);
        VBox doneBox = new VBox();
        doneBox.setPadding(new Insets(20, 0, 0, 0));
        doneBox.setAlignment(Pos.CENTER);
        Button doneButton = new Button("Track done");
        doneButton.getStyleClass().add("doneButton");
        doneButton.setOnAction(e -> {
            track.setDone(true);
            fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
            refreshUI();
        });
        Button reliveButton = new Button("Restart Track");
        reliveButton.getStyleClass().add("reliveButton");
        reliveButton.setOnAction(e -> {
            track.setDone(false);
            fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
            refreshUI();
        });
        doneBox.getChildren().add(!track.isDone() ? doneButton : reliveButton);
        ratingAndButtonBox.getChildren().addAll(topButtonBar, bottomButtonBar, doneBox);
        secondRow.getChildren().addAll(notesVBox, sentToVBox, ratingAndButtonBox);

        // Third Row
        HBox thirdRow = new HBox();
        thirdRow.getStyleClass().add("wip-thirdrow");
        thirdRow.setSpacing(10);
        thirdRow.setPadding(new Insets(5,5,5,5));
        Label tagsHeadline = new Label("Deadline expired!");
        tagsHeadline.setPrefWidth(190);
        if (LocalDate.now().isBefore(track.getDeadline())) {
            long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), track.getDeadline());
            tagsHeadline.setText("Days till Deadline:" + daysBetween + " days.");
            tagsHeadline.getStyleClass().remove("deadlineOver");
        } else {
            tagsHeadline.getStyleClass().add("deadlineOver");
        }
        Region regionAfterTags = new Region();
        regionAfterTags.setPrefHeight(25);
        regionAfterTags.setPrefWidth(350);
        DatePicker deadlineDatePicker = new DatePicker();
        deadlineDatePicker.setPrefHeight(25);
        deadlineDatePicker.setPrefWidth(215);
        deadlineDatePicker.setValue(track.getDeadline());
        deadlineDatePicker.getStyleClass().add("release-deadlilne-date");
        deadlineDatePicker.setPadding(new Insets(0,20,0,0));
        deadlineDatePicker.onActionProperty().set(e -> {
           track.setDeadline(deadlineDatePicker.getValue());
           fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
            if (LocalDate.now().isBefore(track.getDeadline())) {
                long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), track.getDeadline());
                tagsHeadline.setText("Days till Deadline:" + daysBetween + " days.");
                tagsHeadline.getStyleClass().remove("deadlineOver");
            } else {
                tagsHeadline.setText("Deadline expired!");
                tagsHeadline.getStyleClass().add("deadlineOver");
            }
        });
        thirdRow.getChildren().addAll(tagsHeadline, regionAfterTags, deadlineDatePicker);
        HBox deleteTrackHBox = new HBox();
        deleteTrackHBox.setAlignment(Pos.CENTER_RIGHT);
        FontIcon deleteIcon = new FontIcon();
        deleteIcon.setIconLiteral("dashicons-remove");
        deleteIcon.setIconColor(Color.CRIMSON);
        Label deleteTrackLabel = new Label("  delete Track");
        deleteTrackHBox.getChildren().addAll(deleteIcon, deleteTrackLabel);
        deleteTrackHBox.setOnMouseClicked(e -> {
            DeleteAlertBox.display("Delete track?", track.getName() + " will be deleted!");
            if (DeleteAlertBox.answer) {
                dataStore.getTracks().remove(track);
                fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                refreshUI();
            }
        });
        Separator sep = new Separator(Orientation.HORIZONTAL);
        sep.setPrefWidth(200);
        releaseMainVBox.getChildren().addAll(firstRow, secondRow, thirdRow, deleteTrackHBox, sep);
    }
    /**
     * This Method checks if the track to be added is relevant for the set filters and creates the UI-Elements for it.
     * @param track: The Track to be added.
     */
    public void addTrackToWIP(Track track) {
        boolean tag1Check = !ResultFilter.isTag1() || track.getTags().contains(dataStore.getTags().get(0));
        boolean tag2Check = !ResultFilter.isTag2() || track.getTags().contains(dataStore.getTags().get(1));
        boolean tag3Check = !ResultFilter.isTag3() || track.getTags().contains(dataStore.getTags().get(2));
        boolean tag4Check = !ResultFilter.isTag4() || track.getTags().contains(dataStore.getTags().get(3));
        boolean tag5Check = !ResultFilter.isTag5() || track.getTags().contains(dataStore.getTags().get(4));
        boolean textCheck = ResultFilter.getTextFilter().equals("") || track.getName().contains(ResultFilter.getTextFilter());
        if(!tag1Check || !tag2Check || !tag3Check || !tag4Check || !tag5Check || !textCheck) {
            return;
        }

        // First Row
        HBox firstRow = new HBox();
        firstRow.setPrefHeight(48.0);
        firstRow.setPrefWidth(800);
        firstRow.getStyleClass().add("wip-firstrow");
        firstRow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        Label trackTitle = new Label(track.getName() + " (v" + track.getCurrentVersion() + ")");
        trackTitle.getStyleClass().add("tracktitle");
        trackTitle.setPrefWidth(300);
        Region regionBetweenTitleAndPlayer = new Region();
        regionBetweenTitleAndPlayer.setPrefHeight(18.0);
        regionBetweenTitleAndPlayer.setPrefWidth(247.0);
        HBox.setHgrow(regionBetweenTitleAndPlayer, Priority.NEVER);
        FontIcon playPauseButton = new FontIcon();
        playPauseButton.setIconLiteral("dashicons-controls-play");
        playPauseButton.setIconSize(20);
        ProgressBar progressBar = new ProgressBar();
        progressBar.getStyleClass().add("musicprogressbar");
        progressBar.setProgress(0);
        Optional<TrackVersion> latestVersionOptional = track.getFileHistory().stream().filter(version -> version.getVersion() == track.getCurrentVersion()).findFirst();
        HBox playerHBox = new HBox();
        playerHBox.getChildren().add(playPauseButton);
        if (latestVersionOptional.isPresent()) {
            TrackVersion latestVersion = latestVersionOptional.get();
            File latestVersionFile = new File(DropboxPathHelper.convertPath(latestVersion.getPath()));
            if (latestVersionFile.exists()) {
                playerHBox.getChildren().add(progressBar);
                playPauseButton.setOnMouseClicked(e -> {
                    if (playPauseButton.getIconLiteral()
                            .equals("dashicons-controls-pause")) {
                        playPauseButton.setIconLiteral("dashicons-controls-play");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), false, progressBar, playPauseButton);
                    } else {
                        playPauseButton.setIconLiteral("dashicons-controls-pause");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), true, progressBar, playPauseButton);
                    }
                });
            }
            else {
                playPauseButton.setIconLiteral("dashicons-dismiss");
                Label errorLabel = new Label("Previewfile not found! Please Edit and check the Filepath!");
                regionBetweenTitleAndPlayer.setPrefWidth(180);
                playerHBox.getChildren().add(errorLabel);
            }
        }
        firstRow.getChildren().addAll(trackTitle, regionBetweenTitleAndPlayer, playerHBox);
        // Second Row
        HBox secondRow = new HBox();
        secondRow.setPrefHeight(166.0);
        secondRow.setPrefWidth(800);
        secondRow.setSpacing(20);
        secondRow.setPadding(new Insets(5,5,5,0));
        secondRow.getStyleClass().add("wip-secondrow");
        VBox notesVBox = new VBox();
        Label notesLabel = new Label("Notes");
        VBox textAreaVBox = new VBox();
        TextArea notes = new TextArea(track.getNotes());
        Button updateButton = new Button("Save");
        notes.textProperty().addListener(change -> {
            track.setNotes(notes.getText());
                if(!textAreaVBox.getChildren().contains(updateButton)) {
                    updateButton.setVisible(false);
                    updateButton.setVisible(true);
                    textAreaVBox.getChildren().add(updateButton);
                    updateButton.setOnAction(e -> {
                        track.setNotes(notes.getText());
                        fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                        textAreaVBox.getChildren().remove(updateButton);
                    });
                }
            }
        );
        textAreaVBox.getChildren().add(notes);
        notes.setPrefHeight(126);
        notes.setPrefWidth(302);
        notes.setPromptText("Space for ToDos and Notes...");
        notes.getStyleClass().add("wip-notes");
        notesVBox.getChildren().addAll(notesLabel, textAreaVBox);
        VBox sentToVBox = new VBox();
        Label sentToLabel = new Label("Sent to:");
        ListView<String> sentToListView = new ListView<>();
        for(Sendout se : track.getSentTo()) {
            sentToListView.getItems().add(se.getRecipent().getName() + ": " + "v" + se.getVersionSent().getVersion() + " (" + se.getDateSent().format(formatter) + ")");
        }
        sentToListView.setPrefHeight(200);
        sentToListView.setMaxHeight(200);
        sentToListView.setPrefWidth(200);
        sentToListView.getStyleClass().add("wip-sendoutlist");
        sentToVBox.getChildren().addAll(sentToLabel, sentToListView);
        VBox ratingAndButtonBox = new VBox();
        ratingAndButtonBox.setPrefHeight(126);
        ratingAndButtonBox.setPrefWidth(232);
        ratingAndButtonBox.getStyleClass().add("wip-rating-and-button-box");
        Label stars1 = new Label();
        Label stars2 = new Label();
        Label stars3 = new Label();
        Label stars4 = new Label();
        Label stars5 = new Label();
        stars1.setText("🌟");
        stars1.setOnMouseClicked(e -> changeRating(track, 1, stars1, stars2, stars3, stars4, stars5));
        stars2.setText(track.getRating() > 1 ? "🌟" : "⭐");
        stars2.setOnMouseClicked(e -> changeRating(track, 2, stars1, stars2, stars3, stars4, stars5));
        stars3.setText(track.getRating() > 2 ? "🌟" : "⭐");
        stars3.setOnMouseClicked(e -> changeRating(track, 3, stars1, stars2, stars3, stars4, stars5));
        stars4.setText(track.getRating() > 3 ? "🌟" : "⭐");
        stars4.setOnMouseClicked(e -> changeRating(track, 4, stars1, stars2, stars3, stars4, stars5));
        stars5.setText(track.getRating() > 4 ? "🌟" : "⭐");
        stars5.setOnMouseClicked(e -> changeRating(track, 5, stars1, stars2, stars3, stars4, stars5));
        HBox starsHBox = new HBox();
        starsHBox.setAlignment(Pos.CENTER_LEFT);
        starsHBox.setPadding(new Insets(0,0,0,20));
        starsHBox.setPrefHeight(56);
        starsHBox.setPrefWidth(230);
        starsHBox.getStyleClass().add("wip-rating-stars");
        starsHBox.getChildren().addAll(stars1, stars2, stars3, stars4, stars5);
        ButtonBar topButtonBar = new ButtonBar();
        topButtonBar.setPrefHeight(40);
        topButtonBar.setPrefWidth(200);
        topButtonBar.setPadding(new Insets(0,50,5,5));
        Button projectsButton = new Button("Projects");
        projectsButton.getStyleClass().add("wip-projects-button");
        if (track.getProjectFolder() != null && !Objects.equals(track.getProjectFolder(), "")) {
            projectsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getProjectFolder())));
        }
        Button previewsButton = new Button("Previews");
        previewsButton.getStyleClass().add("wip-previews-button");
        if (track.getPreviewFolder() != null && !Objects.equals(track.getPreviewFolder(), "")) {
            previewsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getPreviewFolder())));
        }
        topButtonBar.getButtons().addAll(projectsButton, previewsButton);
        ButtonBar bottomButtonBar = new ButtonBar();
        bottomButtonBar.setPrefHeight(40);
        bottomButtonBar.setPrefWidth(200);
        bottomButtonBar.setLayoutX(10);
        bottomButtonBar.setLayoutY(66);
        bottomButtonBar.setPadding(new Insets(0,50,5,5));
        Button newVersionButton = new Button("Version++");
        newVersionButton.getStyleClass().add("wip-newversion-button");
        newVersionButton.setOnAction(e -> createModalHelper.createVersionModal(this, track));
        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("wip-edit-button");
        editButton.onActionProperty().setValue(e -> createModalHelper.createEditModal(this, track));
        bottomButtonBar.getButtons().addAll(newVersionButton, editButton);
        ratingAndButtonBox.getChildren().addAll(starsHBox, topButtonBar, bottomButtonBar);
        secondRow.getChildren().addAll(notesVBox, sentToVBox, ratingAndButtonBox);

        // Third Row
        HBox thirdRow = new HBox();
        thirdRow.getStyleClass().add("wip-thirdrow");
        thirdRow.setSpacing(10);
        thirdRow.setPadding(new Insets(5,5,5,5));
        Label tagsHeadline = new Label("Tags:");
        Region regionBetweenTagHeadlineAndTag = new Region();
        regionBetweenTagHeadlineAndTag.setPrefHeight(25);
        regionBetweenTagHeadlineAndTag.setPrefHeight(15);
        VBox firstTags = new VBox();
        Label tag1 = new Label(track.getTags().isEmpty() ? "" : track.getTags().get(0).getTagName());
        tag1.setPrefWidth((90));
        tag1.getStyleClass().add("tag");
        if (!track.getTags().isEmpty()) {
            tag1.setStyle("-fx-text-fill: #" + track.getTags().get(0).getTagColor().toString().substring(2, track.getTags().get(0).getTagColor().toString().length()-2));
        }
        Label tag2 = new Label(track.getTags().size() > 1 ? track.getTags().get(1).getTagName() : "");
        tag2.setPrefWidth((90));
        tag2.getStyleClass().add("tag");
        if (track.getTags().size() > 1) {
            tag2.setStyle("-fx-text-fill: #" + track.getTags().get(1).getTagColor().toString().substring(2, track.getTags().get(1).getTagColor().toString().length()-2));
        }
        Label tag3 = new Label(track.getTags().size() > 2 ? track.getTags().get(2).getTagName() : "");
        tag3.setPrefWidth((90));
        tag3.getStyleClass().add("tag");
        if (track.getTags().size() > 2) {
            tag3.setStyle("-fx-text-fill: #" + track.getTags().get(2).getTagColor().toString().substring(2, track.getTags().get(2).getTagColor().toString().length()-2));
        }
        firstTags.getChildren().addAll(tag1, tag2, tag3);
        VBox secondTags = new VBox();
        Label tag4 = new Label(track.getTags().size() > 3 ? track.getTags().get(3).getTagName() : "");
        tag4.setPrefWidth((90));
        tag4.getStyleClass().add("tag");
        if (track.getTags().size() > 3) {
            tag4.setStyle("-fx-text-fill: #" + track.getTags().get(3).getTagColor().toString().substring(2, track.getTags().get(3).getTagColor().toString().length()-2));
        }
        Label tag5 = new Label(track.getTags().size() > 4 ? track.getTags().get(4).getTagName() : "");
        tag5.setPrefWidth((90));
        tag5.getStyleClass().add("tag");
        if (track.getTags().size() > 4) {
            tag5.setStyle("-fx-text-fill: #" + track.getTags().get(4).getTagColor().toString().substring(2, track.getTags().get(4).getTagColor().toString().length()-2));
        }
        secondTags.getChildren().addAll(tag4, tag5);
        Region regionAfterTags = new Region();
        regionAfterTags.setPrefHeight(25);
        regionAfterTags.setPrefWidth(128);
        thirdRow.getChildren().addAll(tagsHeadline, regionBetweenTagHeadlineAndTag, firstTags,secondTags, regionAfterTags);
        HBox deleteTrackHBox = new HBox();
        deleteTrackHBox.setAlignment(Pos.CENTER_RIGHT);
        FontIcon deleteIcon = new FontIcon();
        deleteIcon.setIconLiteral("dashicons-remove");
        deleteIcon.setIconColor(Color.CRIMSON);
        Label deleteTrackLabel = new Label("  delete Track");
        deleteTrackHBox.getChildren().addAll(deleteIcon, deleteTrackLabel);
        deleteTrackHBox.setOnMouseClicked(e -> {
            DeleteAlertBox.display("Delete track?", track.getName() + " will be deleted!");
            if (DeleteAlertBox.answer) {
                dataStore.getTracks().remove(track);
                fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                refreshUI();
            }
        });
        Separator sep = new Separator(Orientation.HORIZONTAL);
        sep.setPrefWidth(200);
        workInProgressMainVBox.getChildren().addAll(firstRow, secondRow, thirdRow, deleteTrackHBox, sep);
    }

    /**
     * This Method checks if the track to be added is relevant for the set filters and creates the UI-Elements for it.
     * @param track: The Track to be added.
     */
    public void addTrackToDrafts(Track track) {
        boolean tag1Check = !ResultFilter.isTag1() || track.getTags().contains(dataStore.getTags().get(0));
        boolean tag2Check = !ResultFilter.isTag2() || track.getTags().contains(dataStore.getTags().get(1));
        boolean tag3Check = !ResultFilter.isTag3() || track.getTags().contains(dataStore.getTags().get(2));
        boolean tag4Check = !ResultFilter.isTag4() || track.getTags().contains(dataStore.getTags().get(3));
        boolean tag5Check = !ResultFilter.isTag5() || track.getTags().contains(dataStore.getTags().get(4));
        boolean textCheck = ResultFilter.getTextFilter().equals("") || track.getName().contains(ResultFilter.getTextFilter());
        if(!tag1Check || !tag2Check || !tag3Check || !tag4Check || !tag5Check || !textCheck) {
            return;
        }

        // First Row
        HBox firstRow = new HBox();
        firstRow.setPrefHeight(48.0);
        firstRow.setPrefWidth(800);
        firstRow.getStyleClass().add("wip-firstrow");
        firstRow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        Label trackTitle = new Label(track.getName() + " (v" + track.getCurrentVersion() + ")");
        trackTitle.getStyleClass().add("tracktitle");
        trackTitle.setPrefWidth(300);
        Region regionBetweenTitleAndPlayer = new Region();
        regionBetweenTitleAndPlayer.setPrefHeight(18.0);
        regionBetweenTitleAndPlayer.setPrefWidth(247.0);
        HBox.setHgrow(regionBetweenTitleAndPlayer, Priority.NEVER);
        FontIcon playPauseButton = new FontIcon();
        playPauseButton.setIconLiteral("dashicons-controls-play");
        playPauseButton.setIconSize(20);
        ProgressBar progressBar = new ProgressBar();
        progressBar.getStyleClass().add("musicprogressbar");
        progressBar.setProgress(0);
        Optional<TrackVersion> latestVersionOptional = track.getFileHistory().stream().filter(version -> version.getVersion() == track.getCurrentVersion()).findFirst();
        HBox playerHBox = new HBox();
        playerHBox.getChildren().add(playPauseButton);
        if (latestVersionOptional.isPresent()) {
            TrackVersion latestVersion = latestVersionOptional.get();
            File latestVersionFile = new File(DropboxPathHelper.convertPath(latestVersion.getPath()));
            if (latestVersionFile.exists()) {
                playerHBox.getChildren().add(progressBar);
                playPauseButton.setOnMouseClicked(e -> {
                    if (playPauseButton.getIconLiteral()
                            .equals("dashicons-controls-pause")) {
                        playPauseButton.setIconLiteral("dashicons-controls-play");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), false, progressBar, playPauseButton);
                    } else {
                        playPauseButton.setIconLiteral("dashicons-controls-pause");
                        togglePlay(new Media(latestVersionFile.toURI()
                                .toString()), true, progressBar, playPauseButton);
                    }
                });
            }
            else {
                playPauseButton.setIconLiteral("dashicons-dismiss");
                Label errorLabel = new Label("Previewfile not found! Please Edit and check the Filepath!");
                regionBetweenTitleAndPlayer.setPrefWidth(180);
                playerHBox.getChildren().add(errorLabel);
            }
        }
        firstRow.getChildren().addAll(trackTitle, regionBetweenTitleAndPlayer, playerHBox);

        // Second Row
        HBox secondRow = new HBox();
        secondRow.setPrefHeight(166.0);
        secondRow.setPrefWidth(800);
        secondRow.setPadding(new Insets(5,5,5,0));
        secondRow.getStyleClass().add("wip-secondrow");
        HBox notesVBox = new HBox();
        Label notesLabel = new Label("Notes");
        notesLabel.setPadding(new Insets(0, 60, 0, 10));
        TextArea notes = new TextArea(track.getNotes());
        VBox textAreaVBox = new VBox();
        Button updateButton = new Button("Save");
        notes.textProperty().addListener(change -> {
                track.setNotes(notes.getText());
                if(!textAreaVBox.getChildren().contains(updateButton)) {
                    updateButton.setVisible(false);
                    updateButton.setVisible(true);
                    textAreaVBox.getChildren().add(updateButton);
                    updateButton.setOnAction(e -> {
                        track.setNotes(notes.getText());
                        fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                        textAreaVBox.getChildren().remove(updateButton);
                    });
                }
            }
        );
        textAreaVBox.getChildren().add(notes);
        notes.setPrefWidth(402);
        notes.setPromptText("Space for ToDos and Notes...");
        notes.getStyleClass().add("wip-notes");
        notesVBox.getChildren().addAll(notesLabel, textAreaVBox);
        VBox ratingAndButtonBox = new VBox();
        ratingAndButtonBox.setPrefHeight(126);
        ratingAndButtonBox.setPrefWidth(232);
        ratingAndButtonBox.getStyleClass().add("wip-rating-and-button-box");
        Label stars1 = new Label();
        Label stars2 = new Label();
        Label stars3 = new Label();
        Label stars4 = new Label();
        Label stars5 = new Label();
        stars1.setText("🌟");
        stars1.setOnMouseClicked(e -> changeRating(track, 1, stars1, stars2, stars3, stars4, stars5));
        stars2.setText(track.getRating() > 1 ? "🌟" : "⭐");
        stars2.setOnMouseClicked(e -> changeRating(track, 2, stars1, stars2, stars3, stars4, stars5));
        stars3.setText(track.getRating() > 2 ? "🌟" : "⭐");
        stars3.setOnMouseClicked(e -> changeRating(track, 3, stars1, stars2, stars3, stars4, stars5));
        stars4.setText(track.getRating() > 3 ? "🌟" : "⭐");
        stars4.setOnMouseClicked(e -> changeRating(track, 4, stars1, stars2, stars3, stars4, stars5));
        stars5.setText(track.getRating() > 4 ? "🌟" : "⭐");
        stars5.setOnMouseClicked(e -> changeRating(track, 5, stars1, stars2, stars3, stars4, stars5));
        HBox starsHBox = new HBox();
        starsHBox.setAlignment(Pos.CENTER_LEFT);
        starsHBox.setPadding(new Insets(0,0,0,20));
        starsHBox.setPrefHeight(56);
        starsHBox.setPrefWidth(230);
        starsHBox.getStyleClass().add("wip-rating-stars");
        starsHBox.getChildren().addAll(stars1, stars2, stars3, stars4, stars5);
        ButtonBar topButtonBar = new ButtonBar();
        topButtonBar.setPrefHeight(40);
        topButtonBar.setPrefWidth(200);
        topButtonBar.setPadding(new Insets(0,5,5,5));
        Button projectsButton = new Button("Projects");
        projectsButton.getStyleClass().add("wip-projects-button");
        if (track.getProjectFolder() != null && !Objects.equals(track.getProjectFolder(), "")) {
            projectsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getProjectFolder())));
        }
        Button previewsButton = new Button("Previews");
        previewsButton.getStyleClass().add("wip-previews-button");
        if (track.getPreviewFolder() != null && !Objects.equals(track.getPreviewFolder(), "")) {
            previewsButton.setOnAction(e -> openFolder(DropboxPathHelper.convertPath(track.getPreviewFolder())));
        }
        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("wip-edit-button");
        editButton.onActionProperty()
                .setValue(e -> createModalHelper.createEditModal(this, track));
        topButtonBar.getButtons().addAll(projectsButton, previewsButton, editButton);
        ratingAndButtonBox.getChildren().addAll(starsHBox, topButtonBar);
        secondRow.getChildren().addAll(notesVBox, ratingAndButtonBox);

        // Third Row
        HBox thirdRow = new HBox();
        thirdRow.getStyleClass().add("wip-thirdrow");
        thirdRow.setSpacing(10);
        thirdRow.setPadding(new Insets(5,5,5,5));
        Label tagsHeadline = new Label("Tags:");
        Region regionBetweenTagHeadlineAndTag = new Region();
        regionBetweenTagHeadlineAndTag.setPrefHeight(25);
        regionBetweenTagHeadlineAndTag.setPrefHeight(15);
        VBox firstTags = new VBox();
        Label tag1 = new Label(track.getTags().isEmpty() ? "" : track.getTags().get(0).getTagName());
        tag1.setPrefWidth((90));
        tag1.getStyleClass().add("tag");
        if (!track.getTags().isEmpty()) {
            tag1.setStyle("-fx-text-fill: #" + track.getTags().get(0).getTagColor().toString().substring(2, track.getTags().get(0).getTagColor().toString().length()-2));
        }
        Label tag2 = new Label(track.getTags().size() > 1 ? track.getTags().get(1).getTagName() : "");
        tag2.setPrefWidth((90));
        tag2.getStyleClass().add("tag");
        if (track.getTags().size() > 2) {
            tag2.setStyle("-fx-text-fill: #" + track.getTags().get(1).getTagColor().toString().substring(2, track.getTags().get(1).getTagColor().toString().length()-2));
        }
        Label tag3 = new Label(track.getTags().size() > 2 ? track.getTags().get(2).getTagName() : "");
        tag3.setPrefWidth((90));
        tag3.getStyleClass().add("tag");
        if (track.getTags().size() > 3) {
            tag3.setStyle("-fx-text-fill: #" + track.getTags().get(2).getTagColor().toString().substring(2, track.getTags().get(2).getTagColor().toString().length()-2));
        }
        firstTags.getChildren().addAll(tag1, tag2, tag3);
        VBox secondTags = new VBox();
        Label tag4 = new Label(track.getTags().size() > 3 ? track.getTags().get(3).getTagName() : "");
        tag4.setPrefWidth((90));
        tag4.getStyleClass().add("tag");
        if (track.getTags().size() > 3) {
            tag4.setStyle("-fx-text-fill: #" + track.getTags().get(3).getTagColor().toString().substring(2, track.getTags().get(3).getTagColor().toString().length()-2));
        }
        Label tag5 = new Label(track.getTags().size() > 4 ? track.getTags().get(4).getTagName() : "");
        tag5.setPrefWidth((90));
        tag5.getStyleClass().add("tag");
        if (track.getTags().size() > 4) {
            tag5.setStyle("-fx-text-fill: #" + track.getTags().get(4).getTagColor().toString().substring(2, track.getTags().get(4).getTagColor().toString().length()-2));
        }
        secondTags.getChildren().addAll(tag4, tag5);
        Region regionAfterTags = new Region();
        regionAfterTags.setPrefHeight(25);
        regionAfterTags.setPrefWidth(128);
        thirdRow.getChildren().addAll(tagsHeadline, regionBetweenTagHeadlineAndTag, firstTags, secondTags, regionAfterTags);
        HBox deleteTrackHBox = new HBox();
        deleteTrackHBox.setAlignment(Pos.CENTER_RIGHT);
        FontIcon deleteIcon = new FontIcon();
        deleteIcon.setIconLiteral("dashicons-remove");
        deleteIcon.setIconColor(Color.CRIMSON);
        Label deleteTrackLabel = new Label("  delete Track");
        deleteTrackHBox.getChildren().addAll(deleteIcon, deleteTrackLabel);
        deleteTrackHBox.setOnMouseClicked(e -> {
            DeleteAlertBox.display("Delete track?", track.getName() + " will be deleted!");
            if (DeleteAlertBox.answer) {
                dataStore.getTracks().remove(track);
                fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
                refreshUI();
            }
        });
        Separator sep = new Separator(Orientation.HORIZONTAL);
        sep.setPrefWidth(200);
        draftsMainVBox.getChildren().addAll(firstRow, secondRow, thirdRow, deleteTrackHBox, sep);
    }

    //TODO: Change Rating System: We dont want to have 5 Labels for each Track. If we still need those, wire them into the Track Object.
    /**
     * This Method changes the rating for a Track
     * @param track: The Track to be rated
     * @param rating: The rating to be set (1-5)
     * @param star1: The Label for Star1
     * @param star2: The Label for Star2
     * @param star3: The Label for Star3
     * @param star4: The Label for Star4
     * @param star5: The Label for Star5
     */
    public void changeRating(Track track, int rating, Label star1, Label star2, Label star3, Label star4, Label star5) {
        star1.setText("🌟");
        star2.setText(rating > 1 ? "🌟" : "⭐");
        star3.setText(rating > 2 ? "🌟" : "⭐");
        star4.setText(rating > 3 ? "🌟" : "⭐");
        star5.setText(rating > 4 ? "🌟" : "⭐");
        track.setRating(rating);
        fileHandler.rewriteFile(settings.getTracksFilename(), Track.class, dataStore.getTracks());
    }

    /**
     * This Method handles the Playback of the Preview-Files
     * @param playThis: The Track to be played
     * @param togglePlay: Boolean for Play or pause
     * @param progressBar: The Progress Bar to be updated
     * @param playPauseButton: The Play/Pause Button to have its Icon refreshed
     */
    public void togglePlay(Media playThis, boolean togglePlay, ProgressBar progressBar, FontIcon playPauseButton) {
        if (togglePlay) {
            player = new MediaPlayer(playThis);
            beginTimer(playThis, progressBar, playPauseButton);
            player.play();
        } else {
            player.stop();
            cancelTimer();
        }
    }

    /**
     * The Timer for Preview-Playback: This Method updates the Progress-Bar.
     * @param playThis: The Track to be played
     * @param progressBar: The UI-Element to be refreshed
     * @param playPauseButton: The UI-Element of Play/Pause Button
     */
    public void beginTimer(Media playThis, ProgressBar progressBar, FontIcon playPauseButton) {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                double current = player.getCurrentTime().toSeconds();
                double end = playThis.getDuration().toSeconds();
                progressBar.setProgress(current/end);
                progressBar.setOnMouseClicked(e -> {
                    double goThere = end / 100 * (e.getX()/1.5);
                    Duration seekIt = new Duration(goThere*1000);
                    player.seek(seekIt);
                });
                if(current/end == 1) {
                    progressBar.setProgress(0);
                    playPauseButton.setIconLiteral("dashicons-controls-play");
                    progressBar.setOnMouseClicked(e -> {
                    });
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Cancels the running Timer.
     */
    public void cancelTimer() {
        timer.cancel();
    }

    /**
     * Shows the Tags Modal
     */
    public void showTagsModal() {
        CreateModalHelper cmh = CreateModalHelper.getInstance();
        cmh.createTagsModal();
    }

    /**
     * Shows the Contact Modal
     */
    public void showContactModal() {
        CreateModalHelper cmh = CreateModalHelper.getInstance();
        cmh.createContactModal();
    }

    /**
     * Shows the Settings Modal
     */
    public void showSettingsModal() {
        CreateModalHelper cmh = CreateModalHelper.getInstance();
        cmh.createSettingsModal();
    }

    /**
     * Refreshes the Filters-UI Elements.
     */
    public void refreshFilter() {
        ResultFilter.setIncludeDone(filterDone.isSelected());
        ResultFilter.setTag1(filterTag01.isSelected());
        ResultFilter.setTag2(filterTag02.isSelected());
        ResultFilter.setTag3(filterTag03.isSelected());
        ResultFilter.setTag4(filterTag04.isSelected());
        ResultFilter.setTag5(filterTag05.isSelected());
        ResultFilter.setTextFilter(filterName.getText());
        refreshUI();
    }

    /**
     * Helper Method to open a Folder in different OS
     */
    private static void openFolder(String folderPath) {
        String osName = System.getProperty("os.name").toLowerCase();
        String command = "";
        if (osName.contains("win")) {
            command = "explorer " + folderPath;
        } else if (osName.contains("mac")) {
            command = "open " + folderPath;
        } else {
            throw new UnsupportedOperationException("Unsupported operating system.");
        }
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}