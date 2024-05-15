package com.renemrhfr.projectorganizer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class DeleteAlertBox {

    private DeleteAlertBox() {

    }

    public static boolean answer;

    public static void display(String title, String message) {
        answer = false;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        window.setMinHeight(200);
        window.setResizable(false);
        window.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("favicon.png"))));
        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Yes");
        yesButton.setPrefWidth(150);
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        Button noButton = new Button("No");
        noButton.setPrefWidth(150);
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        Label noUndoText = new Label("This cannot be undone!");
        VBox layoutVBox = new VBox();
        HBox buttonsHBox = new HBox();
        Region splitter = new Region();
        splitter.setPrefHeight(20);
        buttonsHBox.getChildren().addAll(yesButton, noButton);
        buttonsHBox.setSpacing(20);
        buttonsHBox.setAlignment(Pos.CENTER);
        layoutVBox.getChildren().addAll(label, noUndoText, splitter, buttonsHBox);
        layoutVBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layoutVBox);
        window.setScene(scene);
        window.showAndWait();
    }
}
