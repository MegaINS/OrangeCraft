package ru.megains.engine.graph.light;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLight {

    public Vector4f position;

    public Vector4f ambient;

    public Vector4f diffuse;

    public Vector4f specular;

    public Vector3f attenuation;

    public PointLight() {

          position = new Vector4f();

          ambient= new Vector4f();

          diffuse= new Vector4f();

          specular= new Vector4f();

          attenuation= new Vector3f();

    }

}