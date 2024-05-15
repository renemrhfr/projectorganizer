package com.renemrhfr.projectorganizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static FileHandler instance;

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    public <T> List<T> readFile(String file, Class<T> objectType) {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<T> objectList = (ArrayList<T>) ois.readObject();
            return objectList;
        } catch (FileNotFoundException fnfe) {
            return new ArrayList<>();
        } catch (Exception e) {
            // TODO: Error-Handling!
        }
        return new ArrayList<>();
    }

    public <T> void addToFile(String file, Class<T> objectType, T objectToAdd) {
        List<T> fileContent = readFile(file, objectType);
        if (fileContent == null) {
            fileContent = new ArrayList();
        }
        fileContent.add(objectToAdd);
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
        }
    }

    /**
     * This Method is used to re-write a File with multiple Objects to write.
     * @param file: The File to be written
     * @param objectType: The Object-Type
     * @param objectsToAdd: The actual Object-List
     * @param <T>: May be a Track, a Tag, Contact etc.
     */
    public <T> void rewriteFile(String file, Class<T> objectType, List<T> objectsToAdd) {
        ArrayList<T> fileContent = new ArrayList<>();
        fileContent.addAll(objectsToAdd);
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
        }
    }

    /**
     * This Method is used to re-write a File with a single Object to write.
     * @param file: The File to be written
     * @param objectToAdd: The actual Object
     * @param <T>: May be a Track, a Tag, Contact etc.
     */
    public <T> void rewriteFile(String file, T objectToAdd) {
        ArrayList<T> fileContent = new ArrayList<>();
        fileContent.add(objectToAdd);

        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
        }
    }

    public void initFiles(String ... files) {
        for (String file : files) {
            File f = new File(file);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(null);
            } catch (Exception e) {
                // TODO: Error-Handling!
            }
        }
    }
}
