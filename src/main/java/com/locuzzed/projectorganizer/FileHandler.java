package com.locuzzed.projectorganizer;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static FileHandler instance;

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    public <T> ArrayList<T> readFile(String file, Class<T> objectType) {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            ArrayList<T> objectList = (ArrayList<T>) ois.readObject();
            fis.close();
            ois.close();
            return objectList;
        } catch (FileNotFoundException fnfe) {
            return new ArrayList<>();
        } catch (Exception e) {
            // TODO: Error-Handling!
            System.out.println("Error when reading File: " +e);
        }
        return new ArrayList<>();
    }

    public <T> void addToFile(String file, Class<T> objectType, T objectToAdd) {
        ArrayList<T> fileContent = readFile(file, objectType);
        if (fileContent == null) {
            fileContent = new ArrayList();
        }
        fileContent.add(objectToAdd);
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
            System.out.println("Error during writing File: " +e);
        }
    }

    /**
     * This Method is used to re-write a File with multiple Objects to write.
     * @param file: The File to be written
     * @param objectType: The Object-Type
     * @param objectsToAdd: The actual Object-List
     * @param <T>: May be a Track, a Tag, Contact etc.
     */
    public <T> void rewriteFile(String file, Class<T> objectType, ArrayList<T> objectsToAdd) {
        ArrayList<T> fileContent = new ArrayList<T>();
        fileContent.addAll(objectsToAdd);
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
            System.out.println("Error during re-writing File: " +e);
        }
    }

    /**
     * This Method is used to re-write a File with a single Object to write.
     * @param file: The File to be written
     * @param objectType: The Object-Type
     * @param objectToAdd: The actual Object
     * @param <T>: May be a Track, a Tag, Contact etc.
     */
    public <T> void rewriteFile(String file, Class<T> objectType, T objectToAdd) {
        ArrayList<T> fileContent = new ArrayList<T>();
        fileContent.add(objectToAdd);

        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(fileContent);
        } catch (Exception e) {
            // TODO: Error-Handling!
            System.out.println("Error during re-writing File: " +e);
        }
    }

    public void initFiles(String ... files) {
        for (String file : files) {
            try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                oos.writeObject(null);
            } catch (Exception e) {
                // TODO: Error-Handling!
                System.out.println("Error during initializing files: " +e);
            }
        }

    }
}
