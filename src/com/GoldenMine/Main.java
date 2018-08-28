package com.GoldenMine;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.event.EffectFader;
import com.GoldenMine.event.EffectRotationer;
import com.GoldenMine.graphic.*;
import com.GoldenMine.graphic.elements.*;
import com.GoldenMine.graphic.event.PaletteHandler;
import com.GoldenMine.util.interval.CalculateModelNaturalSin;
import com.GoldenMine.util.interval.Interval;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created by ehe12 on 2018-08-05.
 */
public class Main {
    public static void main(String[] args) {
        Palette palette = new Palette("Test", 60, new Point(500,500), new PaletteHandler() {
            @Override
            public void onFrameStart() {

            }
        });
        //palette.setVSync(true);

        /*float[] positions = new float[]{
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
        };
        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3,
                3, 1, 2,
        };*/
        /*
        float[] positions = new float[] {
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };
        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };


        Mesh mesh = new Mesh(positions, colours, indices);*/

        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,
                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,
                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,};
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,
                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,
                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,};
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};

        ObjectElement element = null;
        ObjectElement element3 = null;
        ObjectElement element5 = null;

        //SpriteData data1 = null;
        //SpriteData data2 = null;
        //SpriteData data3 = null;
        try {
            element = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            ObjectElement element2 = new Mesh(positions, textCoords, indices);

            Sprite button1 = new ButtonSprite(element, element2);
            button1.setPosition(-0.5f,0,-3f);
            //button1.setScale(0.5f);

            SpriteData data1 = palette.addSprite(button1);


             element3 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            ObjectElement element4 = new Mesh(positions, textCoords, indices);

            Sprite button2 = new ButtonSprite(element3, element4);
            button2.setPosition(0.5f,0,-3f);

            SpriteData data2 = palette.addSprite(button2);

            {
                element5 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
                ObjectElement element6 = new Mesh(positions, textCoords, indices);

                Sprite button3 = new ButtonSprite(element5, element6);
                button3.setPosition(0.5f,1,-2f);

                SpriteData data3 = palette.addSprite(button3);
            }

            {
                element5 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
                ObjectElement element6 = new Mesh(positions, textCoords, indices);

                Sprite button3 = new ButtonSprite(element5, element6);
                button3.setPosition(0.5f,-1,-2f);

                SpriteData data3 = palette.addSprite(button3);
            }

            {
                element5 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
                ObjectElement element6 = new Mesh(positions, textCoords, indices);

                Sprite button3 = new ButtonSprite(element5, element6);
                button3.setPosition(-0.5f,1,-2f);

                SpriteData data3 = palette.addSprite(button3);

                new Thread() {
                    public void run() {
                        //data1.addEffect(new EffectFader(), new Interval(0, 240, new CalculateModelNaturalSin()), 1f, 0f);
                        data3.addEffect(new EffectFader(), new Interval(480, 240, new CalculateModelNaturalSin()), 1f, 0f);
                        data1.addEffect(new EffectFader(), new Interval(1000, 240, new CalculateModelNaturalSin()), 1f, 0f);
                        data1.addEffect(new EffectFader(), new Interval(1500, 240, new CalculateModelNaturalSin()), 1f, 0f);

                        for(int i = 0; i < 5; i++) {
                            data1.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xyz", 0f, 360f, 0f, 360f, 0f, 360f);
                            data2.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xyz", 0f, 360f, 0f, 360f, 0f, 360f);
                            data3.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xyz", 0f, 360f, 0f, 360f, 0f, 360f);

                        }
                        //data1.addEffect(new EffectFader(), new Interval(0, 240, new CalculateModelNaturalSin()), 1f, 0f);
                        try {
                            Thread.sleep(5000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



        /*
        ObjectElement element = null;
        ObjectElement element2 = null;
        ObjectElement element3 = null;
        ObjectElement element4 = null;
        ObjectElement element5 = null;
        ObjectElement element6 = null;
        ObjectElement element7 = null;
        try {
            element = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element3 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element5 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element6 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element7 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element4 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            //element3 = new Texture(positions, textCoords, indices, ImageIO.read(new File("resources/textures/grassblock.png")));
            element2 = new Mesh(positions, textCoords, indices);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObjectSprite sprite = new ObjectSprite(element);
        sprite.setPosition(0, 0, 0f);

        ObjectSprite sprite2 = new ObjectSprite(element2);
        sprite2.setPosition(-0.5f, -0.5f, 0f);

        ObjectSprite sprite5 = new ObjectSprite(element5);
        sprite5.setPosition(-0.5f, 0.5f, 0f);

        ObjectSprite sprite6 = new ObjectSprite(element6);
        sprite6.setPosition(0.5f, 0.5f, 0f);

        ObjectSprite sprite7 = new ObjectSprite(element7);
        sprite7.setPosition(0.5f, -0.5f, 0f);


        ObjectSprite sprite3 = new ObjectSprite(element3);
        sprite3.setPosition(1f, 1f, -2f);

        ObjectSprite sprite4 = new ObjectSprite(element4);
        sprite4.setPosition(0f, 0f, -1f);

        //sprite.setRotation(0,30,0);

        SpriteData data = palette.addSprite(sprite);
        //SpriteData data2 = palette.addSprite(sprite2);
        SpriteData data3 = palette.addSprite(sprite3);
        //SpriteData data4 = palette.addSprite(sprite4);
        //SpriteData data5 = palette.addSprite(sprite5);
        //SpriteData data6 = palette.addSprite(sprite6);
        //SpriteData data7 = palette.addSprite(sprite7);

        new Thread() {
            public void run() {
                for(int i = 0; i < 50; i+=3) {
                    data.addEffect(new EffectScaler(), new Interval(i*480, 480, new CalculateModelNaturalSin()), 0f, 1.2f);
                    data.addEffect(new EffectMover(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "x", -1f, 1f);
                    data.addEffect(new EffectMover(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "y", 1f, -1f);
                    //data.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xy", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data.addEffect(new EffectRotationer(), new Interval((i+1)*480, 480, new CalculateModelNaturalSin()), "yz", 0f, 360f, 0f, 360f, 0f, 360f);
                    data.addEffect(new EffectScaler(), new Interval((i+2)*480, 480, new CalculateModelNaturalSin()), 1.2f, 0f);
                    //data.addEffect(new EffectRotationer(), new Interval((i+2)*480, 480, new CalculateModelNaturalSin()), "zx", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data2.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xy", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data2.addEffect(new EffectRotationer(), new Interval((i+1)*480, 480, new CalculateModelNaturalSin()), "yz", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data2.addEffect(new EffectRotationer(), new Interval((i+2)*480, 480, new CalculateModelNaturalSin()), "zx", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data3.addEffect(new EffectRotationer(), new Interval(i*480, 480, new CalculateModelNaturalSin()), "xy", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data3.addEffect(new EffectRotationer(), new Interval((i+1)*480, 480, new CalculateModelNaturalSin()), "yz", 0f, 360f, 0f, 360f, 0f, 360f);
                    //data3.addEffect(new EffectRotationer(), new Interval((i+2)*480, 480, new CalculateModelNaturalSin()), "zx", 0f, 360f, 0f, 360f, 0f, 360f);
                }
            }
        }.start();*/

        palette.start();



        System.exit(0);
    }
}
