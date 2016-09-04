package ru.megains.engine.graph.renderer.gui

import java.awt.Color

import org.joml.Vector3f
import ru.megains.engine.graph.Renderer
import ru.megains.engine.graph.renderer.mesh.{Mesh, MeshMaker}

object Gui {
    val xZero = 0.0f
    val yZero = 0.0f
    val zZero = 0.0f


    def createRect(width: Int, height: Int, color: Color): Mesh = {

        val mm = MeshMaker.getMeshMaker
        mm.startMakeTriangles()
        mm.addColor(color.getRed / 255f, color.getGreen / 255f, color.getBlue / 255f,color.getAlpha / 255f)
        mm.addVertex(xZero, zZero, zZero)
        mm.addVertex(xZero, height, zZero)
        mm.addVertex(width, height, zZero)
        mm.addVertex(width, zZero, zZero)
        mm.addIndex(0, 2, 1)
        mm.addIndex(0, 3, 2)
        mm.makeMesh()
    }

    def createTextureRect(width: Int, height: Int, texture: String): Mesh = {

        val mm = MeshMaker.getMeshMaker
        mm.startMakeTriangles()
        mm.setTexture(texture)
        mm.addVertexWithUV(xZero, zZero, zZero, 0, 1)
        mm.addVertexWithUV(xZero, height, zZero, 0, 0)
        mm.addVertexWithUV(width, height, zZero, 1, 0)
        mm.addVertexWithUV(width, zZero, zZero, 1, 1)
        mm.addIndex(0, 2, 1)
        mm.addIndex(0, 3, 2)
        mm.makeMesh()
    }

    def renderObject(xPos:Int,yPos:Int,mesh: Mesh,renderer:Renderer): Unit ={
        val shaderProgram =  renderer.hudShaderProgram
        shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos,yPos))
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        mesh.render(shaderProgram, renderer.textureManager)
    }


}
