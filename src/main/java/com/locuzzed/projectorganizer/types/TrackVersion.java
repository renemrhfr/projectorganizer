package com.locuzzed.projectorganizer.types;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class TrackVersion implements Serializable, Comparable<TrackVersion> {

    private int version;
    private LocalDate versionDate;

    // Path to the MP3 File (TODO: maybe add ProjectPath aswell?)
    private String path;

    // If true we call to action in the frontend (fix the path)
    private boolean fileLost;

    private UUID belongsToUUID;

    public TrackVersion(int version, LocalDate versionDate, String path, boolean fileLost, UUID belongsToUUID) {
        this.version = version;
        this.versionDate = versionDate;
        this.path = path;
        this.fileLost = fileLost;
        this.belongsToUUID = belongsToUUID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDate getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(LocalDate versionDate) {
        this.versionDate = versionDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFileLost() {
        return fileLost;
    }

    public void setFileLost(boolean fileLost) {
        this.fileLost = fileLost;
    }

    public UUID getBelongsToUUID() {
        return belongsToUUID;
    }

    public void setBelongsToUUID(UUID belongsToUUID) {
        this.belongsToUUID = belongsToUUID;
    }

    @Override
    public String toString() {
        return "TrackVersion{" +
                "version=" + version +
                ", versionDate=" + versionDate +
                ", path='" + path + '\'' +
                ", fileLost=" + fileLost +
                ", belongsToUUID=" + belongsToUUID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackVersion that = (TrackVersion) o;
        return version == that.version && fileLost == that.fileLost && Objects.equals(versionDate, that.versionDate) && Objects.equals(path, that.path) && Objects.equals(belongsToUUID, that.belongsToUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, versionDate, path, fileLost, belongsToUUID);
    }

    @Override
    public int compareTo(TrackVersion o) {
        return Integer.compare(this.version, o.getVersion());
    }
}
