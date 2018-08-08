package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.util.ShaderProgram;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface ObjectElement {
    ShaderProgram getShaderProgram(Palette palette);

    void setShaderProgram(Palette palette, Sprite sprite, ShaderProgram program);



    void render();

    void cleanUp();
}
