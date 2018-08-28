package com.GoldenMine.graphic.util;

import com.GoldenMine.Utility.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL33.GL_ONE_MINUS_SRC1_COLOR;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by ehe12 on 2018-08-07.
 */
public class Window {
    private long window;

    private float FOV;
    private float Z_NEAR;
    private float Z_FAR;

    private boolean resized;

    private boolean opacityModeEnabled = false;

    private Point paletteSize;

    private Matrix4f projectionMatrix;
    private boolean VSync;

    public Window(String title, Point paletteSize) {
        setFOV(67.4f);
        setZNEAR(0.01f);
        setZFAR(1000000f);


        this.paletteSize = paletteSize;
        this.projectionMatrix = new Matrix4f();

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


    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float)paletteSize.getXInt() / (float)paletteSize.getYInt();
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void enableDEPTH() {
        glEnable(GL_DEPTH_TEST);
    }

    public void enableTransperencyFirst() {
        //glEnable(GL_ALPHA_TEST);
        //glAlphaFunc(GL_GREATER, 0.0f);
        //glDepthMask(true);
    }

    public void enableTransperency() {
        if(!opacityModeEnabled) {
            opacityModeEnabled = true;
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //glBlendFunc(GL_DST_ALPHA, GL_ONE_MINUS_DST_ALPHA);
            //glEnable(GL_ALPHA_TEST);
            //glAlphaFunc(GL_GREATER, 1f);
        }
    }

    public void disableTransparency() {
        if(opacityModeEnabled) {
            opacityModeEnabled = false;
            glDisable(GL_BLEND);
        }
    }

    public void clearScreen() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setViewport(int X1, int Y1, int X2, int Y2) {
        glViewport(X1, Y1, X2, Y2);
    }

    public boolean getShouldClose() {
        return glfwWindowShouldClose(window);
    }

    public boolean isResized() {
        if(resized) {
            resized = false;
            return true;
        } else {
            return false;
        }
    }

    public void stopWindow() {
        glfwSetWindowShouldClose(window, true);
    }

    public void swapBuffer() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public Point getPaletteSize() {
        return paletteSize;
    }

    public float getFOV() {
        return FOV;
    }

    public float getZNEAR() {
        return Z_NEAR;
    }

    public float getZFAR() {
        return Z_FAR;
    }

    public void setFOV(float fov) {
        FOV = (float)Math.toRadians(fov);
        resized = true;
    }

    public void setZNEAR(float near) {
        Z_NEAR = near;
        resized = true;
    }

    public void setZFAR(float far) {
        Z_FAR = far;
        resized = true;
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);

    }

    public long getWindowId() {
        return window;
    }

}
