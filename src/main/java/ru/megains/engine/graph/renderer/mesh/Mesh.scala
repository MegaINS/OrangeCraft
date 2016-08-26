package ru.megains.engine.graph.renderer.mesh

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import ru.megains.engine.graph.ShaderProgram
import ru.megains.engine.graph.renderer.texture.TextureManager

import scala.collection.mutable.ArrayBuffer

class Mesh private[mesh](val makeMode: Int,val vertexCount: Int) {

    val vaoId:Int = glGenVertexArrays
    val vboIdList =  ArrayBuffer[Int]()

    def this(makeMode: Int, indices: Array[Int], positions: Array[Float], colours: Array[Float]) {
        this(makeMode,indices.length)
        glBindVertexArray(vaoId)
        bindArray(0,3,positions)
        bindArray(1,3,colours)
        bindArrayIndices(indices)
        glBindVertexArray(0)
    }

    def bindArray( index:Int, size:Int, array: Array[Float]):Unit ={
        val vboId: Int = glGenBuffers
        vboIdList += vboId
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, array, GL_STATIC_DRAW)
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    def bindArrayIndices(array: Array[Int]):Unit ={
        val vboId: Int = glGenBuffers
        vboIdList += vboId
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, array, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }


    private[mesh] def initRender(shaderProgram: ShaderProgram,textureManager:TextureManager) {
        shaderProgram.setUniform("hasTexture", 0)
        glBindVertexArray(vaoId)
        for(id <- 0 to vboIdList.size ){
            glEnableVertexAttribArray(id)
        }
    }

    private[mesh] def endRender() {
        for(id <- 0 to vboIdList.size ){
            glDisableVertexAttribArray(id)
        }
        glBindVertexArray(0)
    }

    def render(shaderProgram: ShaderProgram,textureManager:TextureManager) {
        initRender(shaderProgram,textureManager)
        glDrawElements(makeMode, vertexCount, GL_UNSIGNED_INT, 0)
        endRender()
    }

    def cleanUp() {
        for(id <- 0 to vboIdList.size ){
            glDisableVertexAttribArray(id)
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        for (id <- vboIdList) {
            glDeleteBuffers(id)
        }
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}
