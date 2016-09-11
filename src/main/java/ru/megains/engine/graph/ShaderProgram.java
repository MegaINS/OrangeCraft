package ru.megains.engine.graph;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;
    private final Map<String, UniformData> uniforms;
    private int vertexShaderId;
    private int fragmentShaderId;


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
        uniforms.put(uniformName, new UniformData(uniformLocation));
    }


//    public void createFogUniform(String uniformName) throws Exception {
//        createUniform(uniformName + ".active");
//        createUniform(uniformName + ".colour");
//        createUniform(uniformName + ".density");
//    }

    public void setUniform(String uniformName, Matrix4f value) {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        // Check if float buffer has been created
        FloatBuffer fb = uniformData.getFloatBuffer();
        if (fb == null) {
            fb = BufferUtils.createFloatBuffer(16);
            uniformData.setFloatBuffer(fb);
        }
        // Dump the matrix into a float buffer
        value.get(fb);
        glUniformMatrix4(uniformData.getUniformLocation(), false, fb);
    }

    public void setUniform(String uniformName, Matrix3f value) {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        // Check if float buffer has been created
        FloatBuffer fb = uniformData.getFloatBuffer();
        if (fb == null) {
            fb = BufferUtils.createFloatBuffer(9);
            uniformData.setFloatBuffer(fb);
        }
        // Dump the matrix into a float buffer
        value.get(fb);
        glUniformMatrix3(uniformData.getUniformLocation(), false, fb);
    }


    public void setUniform(String uniformName, int value) {

        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            // System.out.println("Uniform [" + uniformName + "] has nor been created");
            return;
            //  throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1i(uniformData.getUniformLocation(), value);
    }

    public void setUniform(String uniformName, float value) {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1f(uniformData.getUniformLocation(), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform3f(uniformData.getUniformLocation(), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null) {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform4f(uniformData.getUniformLocation(), value.x, value.y, value.z, value.w);
    }
//    public void setUniform(String uniformName, Fog fog) {
//        setUniform(uniformName + ".active", fog.isActive() ? 1 : 0);
//        setUniform(uniformName + ".colour", fog.getColour() );
//        setUniform(uniformName + ".density", fog.getDensity());
//    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Code: " + shaderId);
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
            throw new Exception("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
    }


}
