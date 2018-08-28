package com.GoldenMine.graphic.elements;

import java.awt.Color;
import java.awt.Font;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class Text {
    private Font font;
    private String text;
    private Color color;

    public Text(Font font, String text, Color color) {
        this.font = font;
        this.text = text;
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
