package com.locuzzed.projectorganizer;

import com.locuzzed.projectorganizer.types.Contact;
import com.locuzzed.projectorganizer.types.Tag;
import com.locuzzed.projectorganizer.types.Track;

import java.util.ArrayList;

/**
 * This class is used as a Singleton to have a runtime representation of the Data in use.
 * This way we reduce load on the harddisk, especially helpful for Users who sync Data with Cloudstorage.
 */
public class DataStore {
    private static DataStore instance;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Tag> tags = new ArrayList<>();
    private ArrayList<Track> tracks = new ArrayList<>();

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public ArrayList<Track> getTracks() {
        if (tracks != null) {
            return tracks;
        }
        return new ArrayList<Track>();
    }

    public void setTracks(ArrayList<Track> updatedTracks) {
        tracks = updatedTracks;
    }

    public void setContacts(ArrayList<Contact> updatedContactList) {
        contacts = updatedContactList;
    }

    public ArrayList<Contact> getContacts() {
        if (contacts != null) {
            return contacts;
        }
        return new ArrayList<Contact>();
    }

    public void setTags(ArrayList<Tag> updatedTags) {
        tags = updatedTags;
    }

    public ArrayList<Tag> getTags() {
        if (tags != null) {
            return tags;
        }
        return new ArrayList<Tag>();
    }
}
