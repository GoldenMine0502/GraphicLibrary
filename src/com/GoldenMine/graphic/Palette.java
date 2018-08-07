package com.GoldenMine.graphic;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.util.EffectData;
import com.GoldenMine.util.Utils;
import com.GoldenMine.thread.threadAPI.Delay;
import java.util.ArrayList;
import java.util.HashMap;
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


    private Point paletteSize;
    private int fps;

    private boolean vSync;

    private List<ObjectSprite> sprites = new ArrayList<>();
    private HashMap<ObjectSprite, SpriteData> spriteData = new HashMap<>();

    private ShaderProgram shaderProgram;
    private Transformation transformation;

    private Window window;
    private Camera camera;



    private PaletteHandler paletteHandler;

    private boolean rendering = false;

    public Palette(String title, int fps, Point paletteSize, PaletteHandler handler) {
        this.paletteSize = paletteSize;
        this.fps = fps;
        this.paletteHandler = handler;

        

        transformation = new Transformation();

        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create the window
        window = glfwCreateWindow(paletteSize.getXInt(), paletteSize.getYInt(), title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            paletteSize.setX(width);
            paletteSize.setY(height);
            resized = true;
            //System.out.println("Changed");
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - paletteSize.getXInt()) / 2,
                (vidmode.height() - paletteSize.getYInt()) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();

        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(Utils.loadResource("resources/shaders/texture/vertex.vs"));
            shaderProgram.createFragmentShader(Utils.loadResource("resources/shaders/texture/fragment.fs"));
            shaderProgram.link();

            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("modelViewMatrix");
            shaderProgram.createUniform("texture_sampler");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
    }

    public boolean isResized() {
        if(resized) {
            resized = false;
            return true;
        } else {
            return false;
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void start() {
        Delay delay = new Delay(fps);

        paletteHandler.onRenderStart();

        while(!glfwWindowShouldClose(window)) {
            shaderProgram.bind();

            if(isResized()) {
                glViewport(0, 0, paletteSize.getXInt(), paletteSize.getYInt());

                Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, paletteSize.getXInt(), paletteSize.getYInt(), Z_NEAR, Z_FAR);
                shaderProgram.setUniform("projectionMatrix", projectionMatrix);
            }

            // resized, frame started,

            shaderProgram.setUniform("texture_sampler", 0);

            clearScreen();

            paletteHandler.onFrameStart();


            for(int index = 0; index < sprites.size(); index++) {
                ObjectSprite sprite = sprites.get(index);
                SpriteData data = spriteData.get(sprite);


                data.eventTickPassed();

                List<EffectData> effects = data.getEffects();

                for(int eventIndex = 0; eventIndex < effects.size(); eventIndex++) {
                    EffectData effectData = effects.get(eventIndex);

                    if(effectData.getInterval().isWait())
                        effectData.getEffect().onInvoked(this, sprite, effectData.getInterval().getIntervalPercent(), effectData.getParameters());
                    //System.out.println("invoked");
                }

                Matrix4f worldMatrix = transformation.getModelViewMatrix(sprite, transformation.getViewMatrix(camera));
                shaderProgram.setUniform("modelViewMatrix", worldMatrix);

                sprite.getObjectElement().render();
            }

            swapBuffer();

            paletteHandler.onFrameFinish();

            shaderProgram.unbind();

            if(!vSync) {
                try {
                    delay.autoCompute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        shaderProgram.unbind();


        shaderProgram.cleanUp();

        for(int index = 0; index < sprites.size(); index++) {
            sprites.get(index).getObjectElement().cleanUp();
        }



        paletteHandler.onRenderFinish();
    }

    public boolean shouldClosed() {
        return glfwWindowShouldClose(window);
    }

    public void stop() {
        glfwSetWindowShouldClose(window, true);
        rendering = true;
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

    public void clearScreen() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void swapBuffer() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }
}
