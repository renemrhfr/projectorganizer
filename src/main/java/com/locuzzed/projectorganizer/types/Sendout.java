package com.locuzzed.projectorganizer.types;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Sendout implements Serializable {
    private Contact recipent;
    private LocalDate dateSent;
    private TrackVersion versionSent;

    public Sendout(Contact recipent, LocalDate dateSent, TrackVersion versionSent) {
        this.recipent = recipent;
        this.dateSent = dateSent;
        this.versionSent = versionSent;
    }

    public Contact getRecipent() {
        return recipent;
    }

    public void setRecipent(Contact recipent) {
        this.recipent = recipent;
    }

    public LocalDate getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDate dateSent) {
        this.dateSent = dateSent;
    }

    public TrackVersion getVersionSent() {
        return versionSent;
    }

    public void setVersionSent(TrackVersion versionSent) {
        this.versionSent = versionSent;
    }

    @Override
    public String toString() {
        return "Sendout{" +
                "recipent=" + recipent +
                ", dateSent=" + dateSent +
                ", versionSent=" + versionSent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sendout sendout = (Sendout) o;
        return recipent.equals(sendout.recipent) && dateSent.equals(sendout.dateSent) && versionSent.equals(sendout.versionSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipent, dateSent, versionSent);
    }
}
