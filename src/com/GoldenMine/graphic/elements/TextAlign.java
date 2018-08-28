package com.GoldenMine.graphic.elements;

import com.GoldenMine.Utility.Point;

public enum TextAlign {
      ALIGN_TOPLEFT((xSize, ySize, imageXSize, imageYSize) -> new Point(                              0,                    0))
    , ALIGN_TOPCENTER((xSize, ySize, imageXSize, imageYSize) -> new Point(       (imageXSize-xSize)/2,                    0))
    , ALIGN_TOPRIGHT((xSize, ySize, imageXSize, imageYSize) -> new Point(            imageXSize-xSize,                    0))
    , ALIGN_MIDDLELEFT((xSize, ySize, imageXSize, imageYSize) -> new Point(                         0, (imageYSize-ySize)/2))
    , ALIGN_MIDDLECENTER((xSize, ySize, imageXSize, imageYSize) -> new Point(    (imageXSize-xSize)/2, (imageYSize-ySize)/2))
    , ALIGN_MIDDLERIGHT((xSize, ySize, imageXSize, imageYSize) -> new Point(         imageXSize-xSize, (imageYSize-ySize)/2))
    , ALIGN_BOTTOMLEFT((xSize, ySize, imageXSize, imageYSize) -> new Point(                         0,      (imageYSize-ySize)))
    , ALIGN_BOTTOMCENTER((xSize, ySize, imageXSize, imageYSize) -> new Point(    (imageXSize-xSize)/2,      (imageYSize-ySize)))
    , ALIGN_BOTTOMRIGHT((xSize, ySize, imageXSize, imageYSize) -> new Point(         imageXSize-xSize,      (imageYSize-ySize)))
    ;

    private TextAlignCalculator calculator;



    public Point getStartPosition(int xSize, int ySize, int imageXSize, int imageYSize) {
        return calculator.getStartPosition(xSize, ySize, imageXSize, imageYSize);
    }

    TextAlign(TextAlignCalculator calculator) {
        this.calculator = calculator;
    }

    interface TextAlignCalculator {
        Point getStartPosition(int xSize, int ySize, int imageXSize, int imageYSize);
    }

}
