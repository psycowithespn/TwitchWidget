package com.psyco.twitchwidget;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TileProperties properties = new TileProperties();
        AnchorPane pane = new AnchorPane();
        List<FollowingImage> images = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            FollowingImage instance = new FollowingImage(properties, i);
            images.add(instance);
            pane.getChildren().add(instance.getPane());
        }
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
        primaryStage.sizeToScene();

        KeyFrame frame = new KeyFrame(Duration.seconds(5), (e) -> shuffle(images));
        Timeline line = new Timeline(frame);
        line.setCycleCount(300);
        line.play();
    }

    private void shuffle(List<FollowingImage> images) {
        Collections.shuffle(images);
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setPosition(i);
        }
    }
}
