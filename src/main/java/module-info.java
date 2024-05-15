module com.renemrhfr.projectorganizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires javafx.media;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.dashicons;

    opens com.renemrhfr.projectorganizer to javafx.fxml;
    exports com.renemrhfr.projectorganizer;
    exports com.renemrhfr.projectorganizer.controllers;
    opens com.renemrhfr.projectorganizer.controllers to javafx.fxml;
    exports com.renemrhfr.projectorganizer.types;
    opens com.renemrhfr.projectorganizer.types to javafx.fxml;
}