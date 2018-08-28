package com.GoldenMine.graphic.mouse;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.ObjectElement;
import com.GoldenMine.graphic.elements.Sprite;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluUnProject;

/**
 * Created by ehe12 on 2018-08-10.
 */
public class MousePicker2 {
    public static void select(Palette palette, int x, int y) {

        // The selection buffer
        IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
        int buffer[] = new int[256];

        IntBuffer vpBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
        // The size of the viewport. [0] Is <x>, [1] Is <y>, [2] Is <width>, [3] Is <height>
        int[] viewport = new int[4];

        // The number of "hits" (objects within the pick area).
        int hits;
        // Get the viewport info

        GL11.glGetIntegerv(GL11.GL_VIEWPORT, vpBuffer);
        vpBuffer.get(viewport);


        // Set the buffer that OpenGL uses for selection to our buffer

        GL11.glSelectBuffer(selBuffer);


        // Change to selection mode

        GL11.glRenderMode(GL11.GL_SELECT);


        // Initialize the name stack (used for identifying which object was selected)

        GL11.glInitNames();

        GL11.glPushName(0);


        GL11.glMatrixMode(GL11.GL_PROJECTION);

        GL11.glPushMatrix();

        GL11.glLoadIdentity();



            /*  create 5x5 pixel picking region near cursor location */

        GLU.gluPickMatrix(x, y, 5.0f, 5.0f, IntBuffer.wrap(viewport));


        GLU.gluPerspective((float) Math.toDegrees(palette.getWindow().getFOV()),
                (float) (palette.getPaletteSizeX() / palette.getPaletteSizeY()),
                palette.getWindow().getZNEAR(), palette.getWindow().getZFAR());

        //render();

        GL11.glPopMatrix();


        // Exit selection mode and return to render mode, returns number selected

        hits = GL11.glRenderMode(GL11.GL_RENDER);

        System.out.println("hits: " + hits);


        selBuffer.get(buffer);

        // Objects Were Drawn Where The Mouse Was

        if (hits > 0) {

            // If There Were More Than 0 Hits

            int choose = buffer[3]; // Make Our Selection The First Object

            int depth = buffer[1]; // Store How Far Away It Is

            for (int i = 1; i < hits; i++) {

                // Loop Through All The Detected Hits

                // If This Object Is Closer To Us Than The One We Have Selected

                if (buffer[i * 4 + 1] < depth) {

                    choose = buffer[i * 4 + 3]; // Select The Closer Object

                    depth = buffer[i * 4 + 1]; // Store How Far Away It Is

                }

            }


            System.out.println("Chosen: " + choose + ", depth: " + depth);

        }

    }

    public static FloatBuffer getOGLPos(int mouseX, int mouseY){
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
        float winX, winY;
        FloatBuffer position = BufferUtils.createFloatBuffer(3);
        glGetFloatv(GL_MODELVIEW_MATRIX, modelview);
        glGetFloatv(GL_PROJECTION_MATRIX, projection);
        glGetIntegerv(GL_VIEWPORT, viewport);
        winX = (float)mouseX;
        winY = (float)viewport.get(3) - (float)mouseY;
        glReadPixels(mouseX, (int)winY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, winZ);
        gluUnProject(winX, winY, winZ.get(), modelview, projection, viewport, position);
        return position;
    }

    public static Sprite selectObject(Palette palette, float mouseX, float mouseY, float mouseZ, int ocha){

        for(Sprite obj : palette.getSprites()){
            Vector3f pos = obj.getPosition();
            ObjectElement element = obj.getObjectElement();
            if(mouseX >= pos.x - ocha && mouseX <= pos.x + element.getXMaxSize() + ocha){
                if(mouseY >= pos.y - ocha && mouseY <= pos.y + element.getYMaxSize() + ocha){
                    if(mouseZ >= pos.z - ocha && mouseZ <= pos.z + element.getZMaxSize() + ocha){
                        System.out.println(palette.getSprites().size() + ", " + obj.getPosition());
                        //return obj;
                    }else{
                        //obj.selected = false;
                    }
                }else{
                    //obj.selected = false;
                }
            }else{
                //obj.selected = false;
            }
        }

        return null;
    }

    /*
    public Ray getPickRay(Palette palette, float mouseX, float mouseY) {
        float screenSpaceX = ((2f * mouseX) / (float)palette.getPaletteSizeX() - 1f) * aspectRatio;
        float screenSpaceY = ((-2f * mouseY) / (float)palette.getPaletteSizeY() + 1);
        float zFar = palette.getWindow().getZFAR();
        float zNear = palette.getWindow().getZNEAR();

        float viewRatio = (float) Math.tan(palette.getWindow().getFOV() / 2f);

        screenSpaceX *= viewRatio;
        screenSpaceY *= viewRatio;

        Vector3f cameraSpaceNear = new Vector3f(screenSpaceX * zNear, screenSpaceY * zNear, -zNear);
        Vector3f cameraSpaceFar = new Vector3f(screenSpaceX * zFar, screenSpaceY * zFar, -zFar);

        Matrix4f tmpView = new Matrix4f(palette.getCamera().getViewMatrix());
        Matrix4f invView = tmpView.invert();

        Vector3f worldSpaceNear = new Vector3f();
        Matrix4f.transform(invView, cameraSpaceNear, worldSpaceNear);

        Vector3f worldSpaceFar = new Vector3f();
        Matrix4f.transform(invView, cameraSpaceFar, worldSpaceFar);

        return ray.set(
                new Vector3f(worldSpaceNear.x, worldSpaceNear.y, worldSpaceNear.z),
                new Vector3f(
                        worldSpaceFar.x - worldSpaceNear.x,
                        worldSpaceFar.y - worldSpaceNear.y,
                        worldSpaceFar.z - worldSpaceNear.z
                ).normalize()
        );
    }*/
}
