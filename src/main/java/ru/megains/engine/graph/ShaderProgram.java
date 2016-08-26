package ru.megains.engine.graph;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import ru.megains.engine.graph.light.DirectionalLight;
import ru.megains.engine.graph.light.PointLight;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, UniformData> uniforms;



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

//    public void createPointLightListUniform(String uniformName, int size) throws Exception {
//        for (int i = 0; i < size; i++) {
//            createPointLightUniform(uniformName + "[" + i + "]");
//        }
//    }

//    public void createPointLightUniform(String uniformName) throws Exception {
//        createUniform(uniformName + ".colour");
//        createUniform(uniformName + ".position");
//        createUniform(uniformName + ".intensity");
//        createUniform(uniformName + ".att.constant");
//        createUniform(uniformName + ".att.linear");
//        createUniform(uniformName + ".att.exponent");
//    }

//    public void createSpotLightListUniform(String uniformName, int size) throws Exception {
//        for (int i = 0; i < size; i++) {
//            createSpotLightUniform(uniformName + "[" + i + "]");
//        }
//    }

//    public void createSpotLightUniform(String uniformName) throws Exception {
//        createPointLightUniform(uniformName + ".pl");
//        createUniform(uniformName + ".conedir");
//        createUniform(uniformName + ".cutoff");
//    }

//    public void createDirectionalLightUniform(String uniformName) throws Exception {
//        createUniform(uniformName + ".colour");
//        createUniform(uniformName + ".direction");
//        createUniform(uniformName + ".intensity");
//    }
    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
    }
    public void setUniform(String uniformName, DirectionalLight directionalLight) {
        setUniform(uniformName + ".position", directionalLight.position);
        setUniform(uniformName + ".ambient", directionalLight.ambient);
        setUniform(uniformName + ".diffuse", directionalLight.diffuse);
        setUniform(uniformName + ".specular", directionalLight.specular);
    }
    public void createPointLightUniform(String uniformName)throws Exception {
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".attenuation");

    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".position", pointLight.position);
        setUniform(uniformName + ".ambient", pointLight.ambient);
        setUniform(uniformName + ".diffuse",pointLight.diffuse);
        setUniform(uniformName + ".specular", pointLight.specular);
        setUniform(uniformName + ".attenuation", pointLight.attenuation);
    }
    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".texture");
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".emission");
        createUniform(uniformName + ".shininess");
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".texture", 0);
        setUniform(uniformName + ".ambient",material.ambient);
        setUniform(uniformName + ".diffuse", material.diffuse);
        setUniform(uniformName + ".specular", material.specular);
        setUniform(uniformName + ".emission", material.emission);
        setUniform(uniformName + ".shininess",material.shininess);
    }

//    public void createMaterialUniform(String uniformName) throws Exception {
//        createUniform(uniformName + ".colour");
//        createUniform(uniformName + ".hasTexture");
//        createUniform(uniformName + ".hasNormalMap");
//        createUniform(uniformName + ".reflectance");
//    }

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
        glUniformMatrix4fv(uniformData.getUniformLocation(), false, fb);
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
        glUniformMatrix3fv(uniformData.getUniformLocation(), false, fb);
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
//    public void setUniform(String uniformName, PointLight[] pointLights) {
//        int numLights = pointLights != null ? pointLights.length : 0;
//        for (int i = 0; i < numLights; i++) {
//            setUniform(uniformName, pointLights[i], i);
//        }
//    }

//    public void setUniform(String uniformName, PointLight pointLight, int pos) {
//        setUniform(uniformName + "[" + pos + "]", pointLight);
//    }

//    public void setUniform(String uniformName, PointLight pointLight) {
//        setUniform(uniformName + ".colour", pointLight.getColor());
//        setUniform(uniformName + ".position", pointLight.getPosition());
//        setUniform(uniformName + ".intensity", pointLight.getIntensity());
//        PointLight.Attenuation att = pointLight.getAttenuation();
//        setUniform(uniformName + ".att.constant", att.getConstant());
//        setUniform(uniformName + ".att.linear", att.getLinear());
//        setUniform(uniformName + ".att.exponent", att.getExponent());
//    }

//    public void setUniform(String uniformName, SpotLight[] spotLights) {
//        int numLights = spotLights != null ? spotLights.length : 0;
//        for (int i = 0; i < numLights; i++) {
//            setUniform(uniformName, spotLights[i], i);
//        }
//    }

//    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
//        setUniform(uniformName + "[" + pos + "]", spotLight);
//    }

//    public void setUniform(String uniformName, SpotLight spotLight) {
//        setUniform(uniformName + ".pl", spotLight.getPointLight());
//        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
//        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
//    }

//    public void setUniform(String uniformName, DirectionalLight dirLight) {
//        setUniform(uniformName + ".colour", dirLight.getColor());
//        setUniform(uniformName + ".direction", dirLight.getDirection());
//        setUniform(uniformName + ".intensity", dirLight.getIntensity());
//    }

//    public void setUniform(String uniformName, Material material) {
//        setUniform(uniformName + ".colour", material.getColour());
//        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
//        setUniform(uniformName + ".hasNormalMap", material.hasNormalMap() ? 1 : 0);
//        setUniform(uniformName + ".reflectance", material.getReflectance());
//    }

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
