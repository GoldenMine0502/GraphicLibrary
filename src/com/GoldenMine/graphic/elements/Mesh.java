package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.util.ShaderProgram;
import com.GoldenMine.graphic.util.Transformation;
import com.GoldenMine.util.Utils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements ObjectElement {
    private static ShaderProgram shaderProgram;
    private static Transformation transformation = new Transformation();

    private final int vaoId;

    private final int posVboId;

    private final int colourVboId;

    private final int idxVboId;

    private final int vertexCount;

    private boolean vertexMapping = false;

    private float[] positions;
    private int[] indices;

    float xMaxSize;
    float yMaxSize;
    float zMaxSize;

    // position, color, index

    public Mesh(float[] positions, float[] colours, int[] indices) {
        this.positions = positions;
        this.indices = indices;

        for(int i = 0; i < positions.length; i+=3) {
            float a = Math.abs(positions[i]);
            if (a > xMaxSize)
                xMaxSize = a;

            float b = Math.abs(positions[i+1]);
            if(b > yMaxSize)
                yMaxSize = b;

            float c = Math.abs(positions[i+2]);
            if(c > zMaxSize)
                zMaxSize = c;
        }

        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Colour VBO
            colourVboId = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // unbind buffers
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public ShaderProgram getShaderProgram(Palette palette) {
        if(shaderProgram==null) {
            try {
                shaderProgram = new ShaderProgram();
                shaderProgram.createVertexShader(Utils.loadResource("resources/shaders/vertex/vertex.vs"));
                shaderProgram.createFragmentShader(Utils.loadResource("resources/shaders/vertex/fragment.fs"));
                shaderProgram.link();

                // Create uniforms for world and projection matrices
                shaderProgram.createUniform("projectionMatrix");
                shaderProgram.createUniform("worldMatrix");
                shaderProgram.createUniform("opacity");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return shaderProgram;
    }

    @Override
    public ObjectType getRenderObjectType() {
        return ObjectType.TRIANGLE;
    }

    @Override
    public void setShaderProgram(Palette palette, Sprite sprite,  SpriteData spriteData, ShaderProgram program) {
        Matrix4f worldMatrix = transformation.getWorldMatrix(
                sprite.getPosition(),
                sprite.getRotation(),
                sprite.getScale());
        shaderProgram.setUniform("worldMatrix", worldMatrix);
        shaderProgram.setUniform("opacity", spriteData.getOpacity());
    }


    @Override
    public void render() {
        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colourVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    @Override
    public float[] getPositions() {
        return positions;
    }

    @Override
    public int[] getIndices() {
        return indices;
    }

    @Override
    public float getXMaxSize() {
        return xMaxSize;
    }

    @Override
    public float getYMaxSize() {
        return yMaxSize;
    }

    @Override
    public float getZMaxSize() {
        return zMaxSize;
    }

    public boolean isVertexMapping() {
        return vertexMapping;
    }
}
