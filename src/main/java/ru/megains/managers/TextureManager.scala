package ru.megains.managers

import org.lwjgl.opengl.GL11
import ru.megains.renderer.texture.{ATexture, SimpleTexture, TextureMap}

import scala.collection.mutable

class TextureManager {

    val textureMapBlock = new TextureMap()
    val mapATexture: mutable.HashMap[String, ATexture] = new mutable.HashMap[String, ATexture]


    def bindTexture(name: String) {

        val aTexture: ATexture = getTexture(name)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, aTexture.getGlTextureId)
    }

    def getTexture(name: String): ATexture = {
        mapATexture.getOrElse(name, default = {
            val aTexture = new SimpleTexture(name)
            loadTexture(name, aTexture)
            aTexture
        })
    }

    def loadTexture(name: String, aTexture: ATexture) = {

        if (aTexture.loadTexture()) {
            mapATexture += name -> aTexture
        } else {
            println("Error load texture " + name)
            mapATexture += name -> TextureManager.missingTexture
        }

    }

}

object TextureManager {
    val missingTexture = new SimpleTexture("missing")
    missingTexture.loadTexture()
    val locationBlockTexture: String = "texture/blocks.png"
}
