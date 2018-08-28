package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.util.ShaderProgram;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface ObjectElement {
    ShaderProgram getShaderProgram(Palette palette);

    //public abstract float getOpacity();

    //public abstract float setOpacity(float opacity);

    ObjectType getRenderObjectType();

    void setShaderProgram(Palette palette, Sprite sprite,  SpriteData spriteData, ShaderProgram program);

    void render();

    void cleanUp();

    float[] getPositions();
    int[] getIndices();

    float getXMaxSize();
    float getYMaxSize();
    float getZMaxSize();


}
