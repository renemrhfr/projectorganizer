package com.renemrhfr.projectorganizer.types;

import java.io.Serializable;
import java.util.Objects;

import javafx.scene.paint.Color;

public class Tag implements Serializable {

    private String tagName;

    private transient Color tagColor;

    private SerializableColor tagColorSerializable;

    public Tag(String tagName, Color tagColor) {
        this.tagName = tagName;
        this.tagColor = tagColor;
        this.tagColorSerializable = new SerializableColor(tagColor);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Color getTagColor() {
        if (this.tagColor == null) {
            this.tagColor =this.tagColorSerializable.getFXColor();
        }
        return this.tagColor;
    }

    public void setTagColor(Color tagColor) {
        this.tagColor = tagColor;
        this.tagColorSerializable = new SerializableColor(tagColor);
    }

    public SerializableColor getTagColorSerializable() {
        return tagColorSerializable;
    }

    public void setTagColorSerializable(SerializableColor tagColorSerializable) {
        this.tagColorSerializable = tagColorSerializable;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagName='" + tagName + '\'' +
                ", tagColor='" + tagColor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tagName.equals(tag.tagName) && tagColor.equals(tag.tagColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagColor);
    }
}
