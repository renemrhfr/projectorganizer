package com.renemrhfr.projectorganizer.types;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Track implements Serializable {

    private UUID uuid;

    private String filePath;
    private String name;

    private String notes;
    private LocalDate deadline;

    private TrackStage stage;

    // Path
    private String projectPath;
    private String projectFolder;
    private String previewFolder;
    private boolean onDropbox;

    // If true we call to action in the frontend (fix the path)
    private boolean projectLost;

    private boolean done;

    private List<Tag> tags;

    // 1-5
    private int rating;

    private List<Sendout> sentTo;

    private int currentVersion;
    private List<TrackVersion> fileHistory;

    public Track(String filePath, String previewFolder, String name, String notes, LocalDate deadline, TrackStage stage, String projectPath, String projectFolder, boolean onDropbox, boolean projectLost, List<Tag> tags, int rating, List<Sendout> sentTo, int currentVersion, List<TrackVersion> fileHistory, boolean done) {
        this.filePath = filePath;
        this.previewFolder = previewFolder;
        this.name = name;
        this.notes = notes;
        this.deadline = deadline;
        this.stage = stage;
        this.projectPath = projectPath;
        this.projectFolder = projectFolder;
        this.onDropbox = onDropbox;
        this.projectLost = projectLost;
        this.tags = tags;
        this.rating = rating;
        this.sentTo = sentTo;
        this.currentVersion = currentVersion;
        this.fileHistory = fileHistory;
        this.done = done;

        this.uuid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Track{" +
                "uuid=" + uuid +
                ", filePath='" + filePath + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", deadline=" + deadline +
                ", stage=" + stage +
                ", projectPath='" + projectPath + '\'' +
                ", projectFolder='" + projectFolder + '\'' +
                ", previewFolder='" + previewFolder + '\'' +
                ", onDropbox=" + onDropbox +
                ", projectLost=" + projectLost +
                ", done=" + done +
                ", tags=" + tags +
                ", rating=" + rating +
                ", sentTo=" + sentTo +
                ", currentVersion=" + currentVersion +
                ", fileHistory=" + fileHistory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return onDropbox == track.onDropbox && projectLost == track.projectLost && done == track.done && rating == track.rating && currentVersion == track.currentVersion && Objects.equals(uuid, track.uuid) && Objects.equals(filePath, track.filePath) && Objects.equals(name, track.name) && Objects.equals(notes, track.notes) && Objects.equals(deadline, track.deadline) && stage == track.stage && Objects.equals(projectPath, track.projectPath) && Objects.equals(projectFolder, track.projectFolder) && Objects.equals(previewFolder, track.previewFolder) && Objects.equals(tags, track.tags) && Objects.equals(sentTo, track.sentTo) && Objects.equals(fileHistory, track.fileHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, filePath, name, notes, deadline, stage, projectPath, projectFolder, previewFolder, onDropbox, projectLost, done, tags, rating, sentTo, currentVersion, fileHistory);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPreviewFolder() {
        return previewFolder;
    }

    public void setPreviewFolder(String previewFolder) {
        this.previewFolder = previewFolder;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        if (notes != null) {
            return notes;
        }
        return "";
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDeadline() {
        if (deadline != null) {
            return deadline;
        }
        return LocalDate.of(2099, 1, 1);
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getProjectPath() {
        if (projectPath != null) {
            return projectPath;
        }
        return "";
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getProjectFolder() {
        if (projectFolder != null) {
            return projectFolder;
        }
        return "";
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public boolean isOnDropbox() {
        return onDropbox;
    }

    public void setOnDropbox(boolean onDropbox) {
        this.onDropbox = onDropbox;
    }

    public boolean isProjectLost() {
        return projectLost;
    }

    public void setProjectLost(boolean projectLost) {
        this.projectLost = projectLost;
    }

    public List<Tag> getTags() {
        if (tags != null) {
            return tags;
        }
        return new ArrayList<>();
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Sendout> getSentTo() {
        if (sentTo != null) {
            return sentTo;
        }
        return new ArrayList<>();
    }

    public void setSentTo(List<Sendout> sentTo) {
        this.sentTo = sentTo;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public List<TrackVersion> getFileHistory() {
        if (fileHistory != null) {
            return fileHistory;
        }
        return new ArrayList<>();
    }

    public void setFileHistory(List<TrackVersion> fileHistory) {
        this.fileHistory = fileHistory;
    }

    public TrackStage getStage() {
        if (stage != null) {
            return stage;
        }
        return TrackStage.DRAFT;
    }

    public void setStage(TrackStage stage) {
        this.stage = stage;
    }

    public UUID getUuid() {
        return uuid;
    }

}