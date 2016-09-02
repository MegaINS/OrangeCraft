package ru.megains.engine.graph;

import org.joml.Vector4f;
@Deprecated
public class Material {


    public Texture texture;
    public Vector4f ambient;
    public Vector4f diffuse;
    public Vector4f specular;
    public Vector4f emission;
    public float shininess;

    public Material(Texture texture) {
        this.texture = texture;

        ambient = new Vector4f();
        diffuse = new Vector4f();
        specular = new Vector4f();
        emission = new Vector4f();
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isTextured() {
        return texture != null;
    }


}