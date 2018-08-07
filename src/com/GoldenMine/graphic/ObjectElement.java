package com.GoldenMine.graphic;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface ObjectElement {
    ShaderProgram getShaderProgram(Palette palette);

    void setShaderProgram(Palette palette, ObjectSprite sprite, ShaderProgram program);



    void render();

    void cleanUp();
}
