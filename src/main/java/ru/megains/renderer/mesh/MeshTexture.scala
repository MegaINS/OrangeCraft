package ru.megains.renderer.mesh

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL30._
import ru.megains.engine.graph.ShaderProgram
import ru.megains.game.managers.TextureManager

class MeshTexture(makeMode: Int, val textureName: String, indices: Array[Int], positions: Array[Float], colours: Array[Float], textCoords: Array[Float])
        extends Mesh(makeMode, indices, positions, colours) {
    glBindVertexArray(vaoId)
    bindArray(2, 2, textCoords)
    glBindVertexArray(0)


    override def initRender(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {
        super.initRender(shaderProgram, textureManager)
        shaderProgram.setUniform("hasTexture", 1)
        glActiveTexture(GL_TEXTURE0)
        textureManager.bindTexture(textureName)

        // glBindTexture(GL_TEXTURE_2D, textureId)
    }

    override def endRender(): Unit = {
        super.endRender()
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}
