package com.renemrhfr.projectorganizer;

import com.renemrhfr.projectorganizer.types.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * MockData to be used as Placeholder, when first running the Application,
 */
public class MockData {
    private static MockData instance;

    public static MockData getInstance() {
        if (instance == null) {
            instance = new MockData();
        }
        return instance;
    }
    public static List<Tag> mockTags() {
        Tag tag1 = new Tag("Dancefloor", Color.web("#6279B8"));
        Tag tag2 = new Tag("Banger",  Color.web("#6279B8"));
        Tag tag3 = new Tag("Experimental",  Color.web("#C0BABC"));
        Tag tag4 = new Tag("Epic",  Color.web("#25283D"));
        Tag tag5 = new Tag("Bootleg",  Color.web("#F4743B"));

        ArrayList<Tag> mockTags = new ArrayList<>();
        mockTags.add(tag1);
        mockTags.add(tag2);
        mockTags.add(tag3);
        mockTags.add(tag4);
        mockTags.add(tag5);

        return mockTags;
    }
}
