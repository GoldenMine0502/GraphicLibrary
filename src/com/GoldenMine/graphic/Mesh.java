package com.GoldenMine.graphic;

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

    // position, color, index
    public Mesh(float[] positions, float[] colours, int[] indices) {
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
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return shaderProgram;
    }

    @Override
    public void setShaderProgram(Palette palette, ObjectSprite sprite, ShaderProgram program) {
        Matrix4f worldMatrix = transformation.getWorldMatrix(
                sprite.getPosition(),
                sprite.getRotation(),
                sprite.getScale());
        shaderProgram.setUniform("worldMatrix", worldMatrix);
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

    public boolean isVertexMapping() {
        return vertexMapping;
    }
}
