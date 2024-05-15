package com.renemrhfr.projectorganizer.types;

import java.io.Serializable;
import java.util.Objects;

public class Settings implements Serializable {

    private String userName;
    private String dropboxRoot;

    private static Settings instance;

    private String settingsFilename;
    private String contactsFilename;
    private String tagsFilename;
    private String tracksFilename;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings("New User", "D:\\Dropbox", "Settings.txt", "Contacts.txt", "Tags.txt", "Tracks.txt");
        }
        return instance;
    }

    private Settings(String userName, String dropboxRoot, String settingsFilename, String contactsFilename, String tagsFilename, String tracksFilename) {
        this.userName = userName;
        this.dropboxRoot = dropboxRoot;
        this.settingsFilename = settingsFilename;
        this.contactsFilename = contactsFilename;
        this.tagsFilename = tagsFilename;
        this.tracksFilename = tracksFilename;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDropboxRoot() {
        return dropboxRoot;
    }

    public void setDropboxRoot(String dropboxRoot) {
        this.dropboxRoot = dropboxRoot;
    }

    public String getSettingsFilename() {
        return settingsFilename;
    }

    public void setSettingsFilename(String settingsFilename) {
        this.settingsFilename = settingsFilename;
    }

    public String getContactsFilename() {
        return contactsFilename;
    }

    public void setContactsFilename(String contactsFilename) {
        this.contactsFilename = contactsFilename;
    }

    public String getTagsFilename() {
        return tagsFilename;
    }

    public void setTagsFilename(String tagsFilename) {
        this.tagsFilename = tagsFilename;
    }

    public String getTracksFilename() {
        return tracksFilename;
    }

    public void setTracksFilename(String tracksFilename) {
        this.tracksFilename = tracksFilename;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "userName=" + userName +
                ", dropboxRoot='" + dropboxRoot + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return userName.equals(settings.userName) && dropboxRoot.equals(settings.dropboxRoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, dropboxRoot);
    }


}
