package com.renemrhfr.projectorganizer.types;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {

    private String name;
    private String waysOfContact;

    public Contact(String name, String waysOfContact) {
        this.name = name;
        this.waysOfContact = waysOfContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWaysOfContact() {
        return waysOfContact;
    }

    public void setWaysOfContact(String waysOfContact) {
        this.waysOfContact = waysOfContact;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", waysOfContact='" + waysOfContact + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name) && waysOfContact.equals(contact.waysOfContact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, waysOfContact);
    }
}
