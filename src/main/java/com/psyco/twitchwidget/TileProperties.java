package com.psyco.twitchwidget;

import javafx.beans.property.*;

public class TileProperties {

    private final IntegerProperty tileSize = new SimpleIntegerProperty(200);
    private final DoubleProperty startingScale = new SimpleDoubleProperty(0.8);
    private final IntegerProperty tileBorder = new SimpleIntegerProperty(10);
    private final IntegerProperty numberPerRow = new SimpleIntegerProperty(6);

    public int getTileSize() {
        return tileSize.get();
    }

    public IntegerProperty tileSizeProperty() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize.set(tileSize);
    }

    public double getStartingScale() {
        return startingScale.get();
    }

    public DoubleProperty startingScaleProperty() {
        return startingScale;
    }

    public void setStartingScale(double startingScale) {
        this.startingScale.set(startingScale);
    }

    public int getTileBorder() {
        return tileBorder.get();
    }

    public IntegerProperty tileBorderProperty() {
        return tileBorder;
    }

    public void setTileBorder(int tileBorder) {
        this.tileBorder.set(tileBorder);
    }

    public int getNumberPerRow() {
        return numberPerRow.get();
    }

    public IntegerProperty numberPerRowProperty() {
        return numberPerRow;
    }

    public void setNumberPerRow(int numberPerRow) {
        this.numberPerRow.set(numberPerRow);
    }
}
