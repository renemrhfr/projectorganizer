package com.locuzzed.projectorganizer;

import com.locuzzed.projectorganizer.types.Settings;

import java.io.File;

/**
 * When multiple Users may access the same Track-Data, the Dropbox-Path will be different for each of them.
 * This helper-class makes sure to reference files inside the Dropbox-Context.
 */
public class DropboxPathHelper {
    public static String convertPath(String path) {
        Settings settings = Settings.getInstance();
        path = path.replace("/", File.separator);
        path = path.replace("\\", File.separator);
        if(!path.contains("Dropbox")) {
            return path;
        }
        return settings.getDropboxRoot() + path.substring(path.indexOf("Dropbox")+7);
    }
}
