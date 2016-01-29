package com.psyco.twitchwidget;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageTest extends Application {

    private Group pane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TileProperties properties = new TileProperties();
        pane = new Group();
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
