package com.locuzzed.projectorganizer.types;

import com.locuzzed.projectorganizer.Main;

/**
 * This class is needed to launch the "real" main-class which extends Application.
 * This way the end-user doesnt need to have JavaFX installed, we simply bundle it with the JAR.
 */
public class EntryPoint {

    public static void main(String[] args) {
        Main.main(args);
    }

}
