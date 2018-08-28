package com.GoldenMine.application;

import com.GoldenMine.Utility.ImageUtility;
import com.GoldenMine.Utility.Point;
import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.ObjectSprite;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.Texture;
import com.GoldenMine.graphic.event.PaletteHandler;
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.joml.Vector3f;

/**
 * Created by ehe12 on 2018-08-26.
 */
public class SizeTest {
    public static void main(String[] args) {
        Palette palette = new Palette("TEST", 60, new Point(500, 500), new PaletteHandler() {
            @Override
            public void onRenderStart() {

            }
        });

        BufferedImage image = ImageUtility.createTestImage(300, 300, Color.GREEN);
        Texture texture = new Texture(new float[] {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0,
        }, image);

        ObjectSprite sprite = new ObjectSprite(texture);
        sprite.setPosition(0f, 0f, -1f);
        palette.addSprite(sprite);

        palette.start();
    }

    public static Point getRealPosition(Sprite sprite) {
        Vector3f vec = sprite.getPosition();

        return new Point(0,0);
    }
}
