package com.psyco.twitchwidget;

import com.psyco.twitchwidget.util.MoreBindings;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class FollowingImage {

    private static final Image image = new Image("https://static-cdn.jtvnw.net/jtv_user_pictures/tenshotstv-profile_image-e921186e15b2225a-300x300.png");

    private final TileProperties tileProperties;
    private final ImageView imageView;
    private final BorderPane pane;
    private final IntegerProperty position;

    private Timeline runningHover, runningXChange, runningYChange;
    private final NumberBinding xBinding;
    private final NumberBinding yBinding;

    public FollowingImage(TileProperties tileProperties, int position) {
        this.tileProperties = tileProperties;
        this.position = new SimpleIntegerProperty(position);
        imageView = new ImageView(image);
        imageView.setEffect(new ColorAdjust(0, -1, 0, 0));
        imageView.fitWidthProperty().bind(tileProperties.tileSizeProperty());
        imageView.fitHeightProperty().bind(tileProperties.tileSizeProperty());
        imageView.setScaleX(tileProperties.getStartingScale());
        imageView.setScaleY(tileProperties.getStartingScale());

        tileProperties.startingScaleProperty().addListener(this::startingScaleListener);

        xBinding = buildXBinding();
        yBinding = buildYBinding();
        xBinding.addListener(this::xChangeListener);
        yBinding.addListener(this::yChangeListener);

        pane = new BorderPane(imageView);
        pane.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5)");
        pane.hoverProperty().addListener(this::paneHoverListener);
        pane.setTranslateX(xBinding.getValue().doubleValue());
        pane.setTranslateY(yBinding.getValue().doubleValue());
    }

    public BorderPane getPane() {
        return pane;
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    private void paneHoverListener(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (runningHover != null) {
            runningHover.stop();
        }

        KeyValue color, width, height;

        if (newValue != null && newValue) {
            color = new KeyValue(((ColorAdjust) imageView.getEffect()).saturationProperty(), 0);
            width = new KeyValue(imageView.scaleXProperty(), 1);
            height = new KeyValue(imageView.scaleYProperty(), 1);
        } else {
            color = new KeyValue(((ColorAdjust) imageView.getEffect()).saturationProperty(), -1);
            width = new KeyValue(imageView.scaleXProperty(), tileProperties.getStartingScale());
            height = new KeyValue(imageView.scaleYProperty(), tileProperties.getStartingScale());
        }

        KeyFrame frame = new KeyFrame(Duration.seconds(0.5), color, width, height);
        runningHover = new Timeline(frame);
        runningHover.play();
    }

    private void startingScaleListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (runningHover != null) {
            runningHover.stop();
        }

        imageView.setScaleX(newValue.doubleValue());
        imageView.setScaleY(newValue.doubleValue());
    }

    private NumberBinding buildXBinding() {
        NumberBinding column = MoreBindings.mod(position, tileProperties.numberPerRowProperty());
        NumberBinding sum = Bindings.add(tileProperties.tileBorderProperty(), tileProperties.tileSizeProperty());
        NumberBinding product = Bindings.multiply(sum, column);
        return Bindings.add(tileProperties.tileBorderProperty(), product);
    }

    private NumberBinding buildYBinding() {
        NumberBinding row = MoreBindings.floor(Bindings.divide(position, tileProperties.numberPerRowProperty()));
        NumberBinding sum = Bindings.add(tileProperties.tileBorderProperty(), tileProperties.tileSizeProperty());
        NumberBinding product = Bindings.multiply(sum, row);
        return Bindings.add(tileProperties.tileBorderProperty(), product);
    }

    private void xChangeListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (runningXChange != null) {
            runningXChange.stop();
        }

        KeyValue x = new KeyValue(pane.translateXProperty(), newValue, Interpolator.EASE_BOTH);
        KeyFrame frame = new KeyFrame(Duration.seconds(1), x);
        runningXChange = new Timeline(frame);
        runningXChange.play();
    }

    private void yChangeListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (runningYChange != null) {
            runningYChange.stop();
        }

        KeyValue x = new KeyValue(pane.translateYProperty(), newValue, Interpolator.EASE_BOTH);
        KeyFrame frame = new KeyFrame(Duration.seconds(1), x);
        runningYChange = new Timeline(frame);
        runningYChange.play();
    }
}
