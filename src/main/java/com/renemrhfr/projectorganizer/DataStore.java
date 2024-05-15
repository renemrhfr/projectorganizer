package com.renemrhfr.projectorganizer;

import com.renemrhfr.projectorganizer.types.Contact;
import com.renemrhfr.projectorganizer.types.Tag;
import com.renemrhfr.projectorganizer.types.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a Singleton to have a runtime representation of the Data in use.
 * This way we reduce load on the harddisk, especially helpful for Users who sync Data with Cloudstorage.
 */
public class DataStore {
    private static DataStore instance;
    private List<Contact> contacts = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public List<Track> getTracks() {
        if (tracks != null) {
            return tracks;
        }
        return new ArrayList<>();
    }

    public void setTracks(List<Track> updatedTracks) {
        tracks = updatedTracks;
    }

    public void setContacts(List<Contact> updatedContactList) {
        contacts = updatedContactList;
    }

    public List<Contact> getContacts() {
        if (contacts != null) {
            return contacts;
        }
        return new ArrayList<>();
    }

    public void setTags(List<Tag> updatedTags) {
        tags = updatedTags;
    }

    public List<Tag> getTags() {
        if (tags != null) {
            return tags;
        }
        return new ArrayList<>();
    }
}
