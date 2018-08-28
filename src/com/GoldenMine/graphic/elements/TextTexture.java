package com.GoldenMine.graphic.elements;

import com.GoldenMine.Utility.ImageUtility;
import com.GoldenMine.Utility.Point;
import com.sun.imageio.plugins.common.ImageUtil;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class TextTexture extends Texture {
    BufferedImage source;
    BufferedImage copied;
    //List<Text> texts;

    //List<Point> textPositions;

    Text text;
    Point textPos;

    public TextTexture(float[] positions, BufferedImage image, Text text, Point textPos) {
        super(positions, image);

        this.text = text;
        this.textPos = textPos;
        this.source = image;

        if(image.getData().getDataBuffer() instanceof DataBufferByte) {
            BufferedImage copiedSource = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = copiedSource.getGraphics();
            g.drawImage(image, 0, 0, null);

            this.source = copiedSource;
        }


        update();
    }

    public String getText() {
        return text.getText();
    }

    /*
    public TextTexture(float[] positions, BufferedImage image, Text text, TextAlign align) {
        super(positions, image);
        this.text = text;
        //this.textPos = align.getStartPosition(image.getWidth(), image.getHeight());
        // 팔레트 크기만큼의 높이를 가진 문자 생성
    }*/

    public void setPosition(Point position) {
        textPos.setX(position.getX());
        textPos.setY(position.getY());
        update();
    }

    public void setText(String str) {
        text.setText(str);
        update();
    }

    public void setTextFont(Font font) {
        text.setFont(font);
        update();
    }

    public void setTextColor(Color color) {
        text.setColor(color);
        update();
    }

    public void update() {
        if(copied!=null) {
            copied.flush();
        }
        copied = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        /*int[] raster = ((DataBufferInt)copied.getData().getDataBuffer()).getData();
        int[] originalRaster = ((DataBufferInt)source.getData().getDataBuffer()).getData();

        for(int i = 0; i < raster.length; i++) {
            raster[i] = originalRaster[i];
        }*/
        copied.getGraphics().drawImage(source, 0, 0, null);

        /*JFrame frame = new JFrame();
        frame.setSize(900, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.getGraphics().drawImage(copied, 0, 0, null);*/


        Graphics2D g = (Graphics2D)copied.getGraphics();

        Point size = ImageUtility.getTextSize(text.getFont(), text.getText());

        g.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        g.setFont(text.getFont());
        g.setColor(text.getColor());
        g.drawString(text.getText(), (int)textPos.getX(), (int)(textPos.getY()+size.getY()));

        /* draw texts */
        /*for(int index = 0; index < texts.size(); index++) {
            Text text = texts.get(index);
            Point position = textPositions.get(index);

            BufferedImage image = ImageUtility.createImageFromText(text.getFont(),  text.getText(), text.getColor());
            int[] raster2 = ((DataBufferInt)image.getData().getDataBuffer()).getData();
        }*/

        super.setImage(copied);
        super.updateImage();
    }

    public TextTexture(float[] positions, float[] textCoords, int[] indices, BufferedImage image) {
        super(positions, textCoords, indices, image);
    }
}
