package com.GoldenMine.graphic;

import com.GoldenMine.util.Utils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.dsig.Transform;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by ehe12 on 2018-08-06.
 */
public class Texture implements ObjectElement {
    private static ShaderProgram shaderProgram;
    private static Transformation transformation = new Transformation();

    static {

    }


    int texture;

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    BufferedImage source;

    public Texture(float[] positions, float[] textCoords, int[] indices, BufferedImage image) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            //this.texture = texture;
            vertexCount = indices.length;
            vboIdList = new ArrayList();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }

        source = image;

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // RGBA

        DataBuffer dataBuffer = image.getData().getDataBuffer();
        if (dataBuffer instanceof DataBufferInt) {

            int[] raster = ((DataBufferInt) image.getData().getDataBuffer()).getData();

            for (int i = 0; i < raster.length; i++) {
                int pixel = raster[i];

                buffer.put((byte) (pixel >>> 16));
                buffer.put((byte) (pixel >>> 8));
                buffer.put((byte) (pixel));
                buffer.put((byte) (pixel >>> 24));
            }


        } else if (dataBuffer instanceof DataBufferByte) {
            BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);
            image2.getGraphics().drawImage(image, 0, 0, null);

            int[] raster = ((DataBufferInt) image2.getData().getDataBuffer()).getData();

            for (int i = 0; i < raster.length; i++) {
                int pixel = raster[i];

                buffer.put((byte) (pixel >>> 16));
                buffer.put((byte) (pixel >>> 8));
                buffer.put((byte) (pixel));
                buffer.put((byte) (pixel >>> 24));
            }

        } else {
            throw new RuntimeException("cannot use bufferedimage");
        }

        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(),
                image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glGenerateMipmap(GL_TEXTURE_2D);


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
                shaderProgram.createVertexShader(Utils.loadResource("resources/shaders/texture/vertex.vs"));
                shaderProgram.createFragmentShader(Utils.loadResource("resources/shaders/texture/fragment.fs"));
                shaderProgram.link();

                // Create uniforms for modelView and projection matrices and texture
                shaderProgram.createUniform("projectionMatrix");
                shaderProgram.createUniform("modelViewMatrix");
                shaderProgram.createUniform("texture_sampler");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return shaderProgram;
    }

    @Override
    public void setShaderProgram(Palette palette, ObjectSprite sprite, ShaderProgram program) {
        shaderProgram.setUniform("texture_sampler", 0);
        Matrix4f worldMatrix = transformation.getModelViewMatrix(sprite, transformation.getViewMatrix(palette.getCamera()));
        shaderProgram.setUniform("modelViewMatrix", worldMatrix);
    }

    @Override
    public void render() {
        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture);

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
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        //texture.cleanup();

        glDeleteTextures(texture);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

        if(shaderProgram!=null) {
            shaderProgram.cleanUp();
        }
    }
}
