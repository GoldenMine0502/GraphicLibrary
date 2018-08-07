package com.GoldenMine.graphic;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        //System.out.println(uniformName);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            /*

            glUniform{1|2|3|4}{f|i|ui}{없음 or v}
            number of components in the data type of the specified uniform variable
            (e.g., 1 for float, int, unsigned int, bool; 2 for vec2, ivec2, uvec2, bvec2, etc.)

            The suffix f indicates that floating-point values are being passed;
            the suffix i indicates that integer values are being passed;
            the suffix ui indicates that unsigned integer values are being passed,

            and this type should also match the data type of the specified uniform variable.
            The i variants of this function should be used to provide values for uniform variables defined as int, ivec2, ivec3, ivec4, or arrays of these.
            The ui variants of this function should be used to provide values for uniform variables defined as unsigned int, uvec2, uvec3, uvec4, or arrays of these.
            The f variants should be used to provide values for uniform variables of type float, vec2, vec3, vec4, or arrays of these.

            Either the i, ui or f variants may be used to provide values for uniform variables of type bool, bvec2, bvec3, bvec4, or arrays of these. The uniform variable will be set to false if the input value is 0 or 0.0f, and it will be set to true otherwise.

            v는 하나의 값을 수정할 때 사용됨
            v안붙으면 모든값 한방에 집어넣을때 사용됨

            Matrix 붙으면 glUniformMatrix{1,2,3,4}f = 1x1,2x2,3x3,4x4 크기.


             */
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
            //glUniformMatrix4x3fv();
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanUp() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
