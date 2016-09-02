package ru.megains.engine.graph;


import org.newdawn.slick.opengl.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
@Deprecated
public class Texture {

    private int id;

    private int width;

    private int height;

    public void setId(int id) {
        this.id = id;
    }


    public Texture(int width, int height, int pixelFormat) throws Exception {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        //  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public Texture(String fileName) {
        this(Texture.class.getResourceAsStream(fileName));
    }

    public Texture(int id) {
        this.id = id;
    }

    public Texture(InputStream is) {


        // Load Texture file
        PNGDecoder decoder = null;
        try {
            decoder = new PNGDecoder(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.width = decoder.getWidth();
        this.height = decoder.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }


        buf.flip();


        // Create a new OpenGL texture
        this.id = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, this.id);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
