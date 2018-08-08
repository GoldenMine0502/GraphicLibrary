package com.GoldenMine.graphic;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.graphic.camera.Camera;
import com.GoldenMine.graphic.camera.MouseBoxSelectionDetector;
import com.GoldenMine.graphic.elements.ObjectElement;
import com.GoldenMine.graphic.elements.SpriteData;
import com.GoldenMine.graphic.event.PaletteHandler;
import com.GoldenMine.graphic.elements.Clickable;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.util.ShaderProgram;
import com.GoldenMine.graphic.util.Transformation;
import com.GoldenMine.graphic.util.Window;
import com.GoldenMine.util.EffectData;
import com.GoldenMine.thread.threadAPI.Delay;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ehe12 on 2018-08-05.
 */
public class Palette {
    private Window window;
    private Camera camera;

    private MouseBoxSelectionDetector selectionDetector;

    private Point paletteSize;
    private int fps;

    private boolean vSync;

    private List<Sprite> sprites = new ArrayList<>();
    private HashMap<Sprite, SpriteData> spriteData = new HashMap<>();

    private Transformation transformation;

    private PaletteHandler paletteHandler;

    public Palette(String title, int fps, Point paletteSize, PaletteHandler handler) {
        this.paletteSize = paletteSize;
        this.fps = fps;
        this.paletteHandler = handler;

        this.window = new Window(title, paletteSize);

        this.camera = new Camera();

        this.selectionDetector = new MouseBoxSelectionDetector();

        transformation = new Transformation();

        // Set the clear color
        window.setClearColor(0,0,0,0);
        window.enableDEPTH();

        glfwSetMouseButtonCallback(window.getWindowId(), (window, button, action, mods) -> {
            if(button == GLFW_MOUSE_BUTTON_1) {
                if(action == GLFW_PRESS) {
                    double posX;
                    double posY;

                    try(MemoryStack stack = MemoryStack.stackPush()) {
                        DoubleBuffer posXBuffer = stack.mallocDouble(8);
                        DoubleBuffer posYBuffer = stack.mallocDouble(8);

                        glfwGetCursorPos(window, posXBuffer, posYBuffer);
                        posX = posXBuffer.get();
                        posY = posYBuffer.get();
                    }

                    //System.out.println(((float)(posX*2/paletteSize.getXInt())-1.f) + ", " + ((float)(2-posY*2/paletteSize.getYInt())-1.f));

                    //System.out.println(camera);
                    camera.updateViewMatrix();

                    if(this.window != null) {

                        Sprite sprite = selectionDetector.selectGameItem(sprites, this.window, (int)posX, (int)posY, camera);

                    }
                    //System.out.println(sprite!=null ? sprite.getPosition() : "null");
                } else if(action == GLFW_RELEASE) {
                    for(int i = 0; i < sprites.size(); i++) {
                        Sprite sprite = sprites.get(i);

                        if(sprite instanceof Clickable) {
                            ((Clickable) sprite).setClicked(false);
                        }
                    }
                }
            }
        });
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
                window.updateProjectionMatrix();
            }



            for(int index = 0; index < sprites.size(); index++) {

                Sprite sprite = sprites.get(index);
                SpriteData data = spriteData.get(sprite);

                ObjectElement objectElement = sprite.getObjectElement();

                ShaderProgram shaderProgram = objectElement.getShaderProgram(this);
                shaderProgram.bind();

                if(isResized) {
                    Matrix4f projectionMatrix = transformation.getProjectionMatrix(window.getFOV(), paletteSize.getXInt(), paletteSize.getYInt(), window.getZNEAR(), window.getZFAR());
                    shaderProgram.setUniform("projectionMatrix", projectionMatrix);
                }
                objectElement.setShaderProgram(this, sprite, shaderProgram);

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

    public SpriteData addSprite(Sprite sprite) {
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
