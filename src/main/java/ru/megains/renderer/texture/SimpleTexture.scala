package ru.megains.renderer.texture

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL30._
import org.newdawn.slick.opengl.PNGDecoder

class SimpleTexture(val name: String) extends ATexture {


    override def loadTexture(): Boolean = {

        val is = getClass.getResourceAsStream("/textures/" + name + ".png")
        if (is != null) {
            val png = new PNGDecoder(is)
            val width = png.getWidth
            val height = png.getHeight
            val byteByf = ByteBuffer.allocateDirect(width * height * 4)
            png.decode(byteByf, width * 4, PNGDecoder.RGBA)
            byteByf.flip()

            glBindTexture(GL_TEXTURE_2D, getGlTextureId)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteByf)
            glGenerateMipmap(GL_TEXTURE_2D)
            true
        } else {
            false
        }

    }
}
