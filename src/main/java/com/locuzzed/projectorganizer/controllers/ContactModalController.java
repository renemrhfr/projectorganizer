package com.locuzzed.projectorganizer.controllers;

import com.locuzzed.projectorganizer.DataStore;
import com.locuzzed.projectorganizer.FileHandler;
import com.locuzzed.projectorganizer.types.Contact;
import com.locuzzed.projectorganizer.types.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactModalController {

    @FXML
    TextField contactModalContactNameTextInput;

    @FXML
    TextField contactModalContactDetailsTextArea;

    @FXML
    Button contactModalAddContactButton;

    @FXML
    ListView contactModalContactList;

    @FXML
    Button contactModalClose;

    @FXML
    Button contactModalRemoveButton;

    List<Contact> sortedContacts = new ArrayList<>();

    DataStore dataStore = DataStore.getInstance();
    FileHandler fileHandler = FileHandler.getInstance();
    Settings settings = Settings.getInstance();

    public void initialize() {
        List<Contact> contactList = dataStore.getContacts();
        sortedContacts.clear();
        sortedContacts.addAll(contactList.stream()
                .sorted(Comparator.comparing(Contact::getName))
                .toList());

        contactModalContactList.getItems()
                .addAll(sortedContacts.stream()
                        .map(contact -> contact.getName() + " | " + contact.getWaysOfContact())
                        .toList()
                );

        contactModalContactList.getSelectionModel().selectedItemProperty().addListener(e -> contactModalRemoveButton.setDisable(false));
    }

    public void addContact() {
        if (contactModalContactNameTextInput.getText() != null && contactModalContactDetailsTextArea.getText() != null) {
            Contact contact = new Contact(contactModalContactNameTextInput.getText(),contactModalContactDetailsTextArea.getText());
            dataStore.getContacts().add(contact);
            contactModalContactList.getItems().clear();
            fileHandler.addToFile(settings.getContactsFilename(), Contact.class, contact);
            initialize();
        }
    }

    public void removeContact() {
        int indexToRemove = contactModalContactList.getSelectionModel().getSelectedIndex();
        Contact toRemove =  sortedContacts.get(indexToRemove);
        sortedContacts.remove(toRemove);
        dataStore.getContacts().remove(toRemove);
        contactModalContactList.getItems().remove(contactModalContactList.getSelectionModel().getSelectedIndex());
        contactModalContactList.getSelectionModel().clearSelection();
        fileHandler.rewriteFile(settings.getContactsFilename(), Contact.class, dataStore.getContacts());
    }

}
