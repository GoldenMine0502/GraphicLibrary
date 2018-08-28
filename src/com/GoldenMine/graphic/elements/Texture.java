package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.util.ShaderProgram;
import com.GoldenMine.graphic.util.Transformation;
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
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
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


    //BufferedImage source;
    int texture;

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private BufferedImage source;
    //private BufferedImage editImage;

    private int[] originalRaster;

    private float[] positions;
    private int[] indices;

    float xMaxSize;
    float yMaxSize;
    float zMaxSize;

    public static float[] getDefaultTex() {
        return new float[] {
                0.0f, 0.0f,
                0.0f, 1f,
                1f, 1f,
                1f, 0.0f
        };
    }

    public static int[] getDefaultIndices() {
        return new int[] {
                0, 1, 3, 3, 1, 2
        };
    }

    public Texture(float[] positions, BufferedImage image) {
        this(positions, getDefaultTex(), getDefaultIndices(), image);
    }

    public Texture(float[] positions, float[] textCoords, int[] indices, BufferedImage image) {
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
        if (dataBuffer instanceof DataBufferByte) {
            BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            image2.getGraphics().drawImage(image, 0, 0, null);

            source = image2;
            image = image2;
        }
        //if (dataBuffer instanceof DataBufferInt) {

        originalRaster = ((DataBufferInt) image.getData().getDataBuffer()).getData();
        //editImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < originalRaster.length; i++) {
            int pixel = originalRaster[i];

            buffer.put((byte) (pixel >>> 16));
            buffer.put((byte) (pixel >>> 8));
            buffer.put((byte) pixel);
            buffer.put((byte) (pixel >>> 24));
            //buffer.put((byte) (pixel >>> 24));
        }

        buffer.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL_RGBA, GL_UNSIGNED_INT, raster2);

        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(),image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //glTexSubImage2D(getTarget(), 0, x, y, width, height, dataFormat, GL_UNSIGNED_BYTE, data);


        //glEnable(GL_BLEND);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        /*
        You can use glTexSubImage2D to modify an already existing texture.
         */

        //glTexSubImage2D();


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);

        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );

        glGenerateMipmap(GL_TEXTURE_2D);


    }

    public void setImage(BufferedImage image) {
        if(source!=null) {
            source.flush();
        }
        this.source = image;
    }

    public void updateImage() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(source.getWidth() * source.getHeight() * 4); // RGBA

        DataBuffer dataBuffer = source.getData().getDataBuffer();
        if (dataBuffer instanceof DataBufferByte) {
            BufferedImage image2 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
            image2.getGraphics().drawImage(source, 0, 0, null);

            source = image2;
        }
        //if (dataBuffer instanceof DataBufferInt) {

        originalRaster = ((DataBufferInt) source.getData().getDataBuffer()).getData();
        //editImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < originalRaster.length; i++) {
            int pixel = originalRaster[i];

            buffer.put((byte) (pixel >>> 16));
            buffer.put((byte) (pixel >>> 8));
            buffer.put((byte) pixel);
            buffer.put((byte) (pixel >>> 24));
            //buffer.put((byte) (pixel >>> 24));
        }

        buffer.flip();


        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, source.getWidth(), source.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public ShaderProgram getShaderProgram(Palette palette) {
        if (shaderProgram == null) {
            try {
                shaderProgram = new ShaderProgram();
                shaderProgram.createVertexShader(Utils.loadResource("resources/shaders/texture/vertex.vs"));
                shaderProgram.createFragmentShader(Utils.loadResource("resources/shaders/texture/fragment.fs"));
                shaderProgram.link();

                // Create uniforms for modelView and projection matrices and texture
                shaderProgram.createUniform("projectionMatrix");
                shaderProgram.createUniform("modelViewMatrix");
                shaderProgram.createUniform("texture_sampler");
                shaderProgram.createUniform("opacity");
            } catch (Exception ex) {
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
        shaderProgram.setUniform("texture_sampler", 0);
        Matrix4f worldMatrix = transformation.getModelViewMatrix(sprite, transformation.getViewMatrix(palette.getCamera()));
        shaderProgram.setUniform("modelViewMatrix", worldMatrix);
        shaderProgram.setUniform("opacity", spriteData.getOpacity());
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
        //glDisableVertexAttribArray(0);

        // Delete the VBOs
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
        //for (int vboId : vboIdList) {
            //glDeleteBuffers(vboId);
        //}

        // Delete the texture
        //texture.cleanup();

        glDeleteTextures(texture);
        //System.out.println("success");
        // Delete the VAO
        //glBindVertexArray(0);
        //glDeleteVertexArrays(vaoId);
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
}
