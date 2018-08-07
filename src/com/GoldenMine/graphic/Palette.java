package com.GoldenMine.graphic;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.util.EffectData;
import com.GoldenMine.util.Utils;
import com.GoldenMine.thread.threadAPI.Delay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by ehe12 on 2018-08-05.
 */
public class Palette {
    private Window window;
    private Camera camera;

    private Point paletteSize;
    private int fps;

    private boolean vSync;

    private List<ObjectSprite> sprites = new ArrayList<>();
    private HashMap<ObjectSprite, SpriteData> spriteData = new HashMap<>();

    private Transformation transformation;

    private PaletteHandler paletteHandler;

    public Palette(String title, int fps, Point paletteSize, PaletteHandler handler) {
        this.paletteSize = paletteSize;
        this.fps = fps;
        this.paletteHandler = handler;

        this.window = new Window(title, paletteSize);

        transformation = new Transformation();

        // Set the clear color
        window.setClearColor(0,0,0,0);
        window.enableDEPTH();
    }


    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void start() {
        Delay delay = new Delay(fps);

        paletteHandler.onRenderStart();

        while(!window.getShouldClose()) {
            //shaderProgram.bind();



            window.clearScreen();

            paletteHandler.onFrameStart();

            //boolean resized = ;

            /*
            if(window.isResized()) {
                // update window viewport
                window.setViewport(0, 0, paletteSize.getXInt(), paletteSize.getYInt());

                // update shaderprogram settings
                HashSet<ShaderProgram> setted = new HashSet<ShaderProgram>();

                for(int index = 0; index < sprites.size(); index++) {
                    ShaderProgram shaderProgram = sprites.get(index).getObjectElement().getShaderProgram(this);

                    if(!setted.contains(shaderProgram)) {
                        setted.add(shaderProgram);

                        Matrix4f projectionMatrix = transformation.getProjectionMatrix(window.getFOV(), paletteSize.getXInt(), paletteSize.getYInt(), window.getZNEAR(), window.getZFAR());
                        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
                    }
                }
                //System.out.println(setted.size());
            }*/

            boolean isResized = window.isResized();

            if(isResized) {
                window.setViewport(0, 0, paletteSize.getXInt(), paletteSize.getYInt());
            }

            for(int index = 0; index < sprites.size(); index++) {

                ObjectSprite sprite = sprites.get(index);
                SpriteData data = spriteData.get(sprite);

                ObjectElement objectElement = sprite.getObjectElement();

                ShaderProgram shaderProgram = objectElement.getShaderProgram(this);
                shaderProgram.bind();

                if(isResized) {
                    Matrix4f projectionMatrix = transformation.getProjectionMatrix(window.getFOV(), paletteSize.getXInt(), paletteSize.getYInt(), window.getZNEAR(), window.getZFAR());
                    shaderProgram.setUniform("projectionMatrix", projectionMatrix);
                }
                objectElement.setShaderProgram(this, sprite, shaderProgram);

                /*
                shaderProgram 생성이 Mesh 생성 타이밍보다 느리다.
                 */

                data.eventTickPassed();
                List<EffectData> effects = data.getEffects();
                for(int eventIndex = 0; eventIndex < effects.size(); eventIndex++) {
                    EffectData effectData = effects.get(eventIndex);
                    if(effectData.getInterval().isWait())
                        effectData.getEffect().onInvoked(this, sprite, effectData.getInterval().getIntervalPercent(), effectData.getParameters());
                }

                objectElement.render();

                shaderProgram.unbind();
            }

            window.swapBuffer();

            paletteHandler.onFrameFinish();

            //shaderProgram.unbind();

            if(!vSync) {
                try {
                    delay.autoCompute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        //shaderProgram.unbind();
        //shaderProgram.cleanUp();

        for(int index = 0; index < sprites.size(); index++) {
            sprites.get(index).getObjectElement().cleanUp();
        }
        paletteHandler.onRenderFinish();
    }

    public void stop() {
        window.stopWindow();
    }

    public void setVSync(boolean istrue) {
        vSync = istrue;
        glfwSwapInterval(vSync ? 1 : 0);
    }

    public SpriteData addSprite(ObjectSprite sprite) {
        sprites.add(sprite);

        SpriteData data = new SpriteData();
        spriteData.put(sprite, data);

        return data;
    }

    public Camera getCamera() {
        return camera;
    }

    public Window getWindow() {
        return window;
    }
}
