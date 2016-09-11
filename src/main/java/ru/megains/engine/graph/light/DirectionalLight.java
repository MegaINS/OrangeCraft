package ru.megains.engine.graph.light;

import org.joml.Vector4f;

public class DirectionalLight {


    public Vector4f position;

    public Vector4f ambient;

    public Vector4f diffuse;

    public Vector4f specular;
    OrthoCoords orthoCords;
    private float shadowPosMult = 5;

    public DirectionalLight() {

        position = new Vector4f();

        ambient = new Vector4f();

        diffuse = new Vector4f();

        specular = new Vector4f();
        orthoCords = new OrthoCoords();

    }

    public float getShadowPosMult() {
        return shadowPosMult;
    }

    public OrthoCoords getOrthoCoords() {

        return orthoCords;
    }

    public void setOrthoCords(float left, float right, float bottom, float top, float near, float far) {
        orthoCords.left = left;
        orthoCords.right = right;
        orthoCords.bottom = bottom;
        orthoCords.top = top;
        orthoCords.near = near;

    }

    public static class OrthoCoords {

        public float left;

        public float right;

        public float bottom;

        public float top;

        public float near;

        public float far;
    }
}