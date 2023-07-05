module com.locuzzed.projectorganizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires javafx.media;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.dashicons;

    opens com.locuzzed.projectorganizer to javafx.fxml;
    exports com.locuzzed.projectorganizer;
    exports com.locuzzed.projectorganizer.controllers;
    opens com.locuzzed.projectorganizer.controllers to javafx.fxml;
    exports com.locuzzed.projectorganizer.types;
    opens com.locuzzed.projectorganizer.types to javafx.fxml;
}