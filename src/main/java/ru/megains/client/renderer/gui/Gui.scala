package ru.megains.client.renderer.gui

import java.awt.Color

import ru.megains.client.renderer.EntityRenderer
import ru.megains.client.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.common.util.Vec3f


trait Gui {

    val xZero = 0.0f
    val yZero = 0.0f
    val zZero = 0.0f


    def createRect(width: Int, height: Int, color: Color): Mesh = {

        val mm = MeshMaker.getMeshMaker
        mm.startMakeTriangles()
        mm.addColor(color.getRed / 255f, color.getGreen / 255f, color.getBlue / 255f, color.getAlpha / 255f)
        mm.addVertex(xZero, zZero, zZero)
        mm.addVertex(xZero, height, zZero)
        mm.addVertex(width, height, zZero)
        mm.addVertex(width, zZero, zZero)
        mm.addIndex(0, 2, 1)
        mm.addIndex(0, 3, 2)
        mm.makeMesh()
    }

    def createGradientRect(width: Int, height: Int, color1: Color, color2: Color): Mesh = {
        val mm = MeshMaker.getMeshMaker
        mm.startMakeTriangles()
        mm.addColorRGBA(color2.getRed, color2.getGreen, color2.getBlue, color2.getAlpha)
        mm.addVertex(xZero, zZero, zZero)
        mm.addColorRGBA(color1.getRed, color1.getGreen, color1.getBlue, color1.getAlpha)
        mm.addVertex(xZero, height, zZero)
        mm.addColorRGBA(color1.getRed, color1.getGreen, color1.getBlue, color1.getAlpha)
        mm.addVertex(width, height, zZero)
        mm.addColorRGBA(color2.getRed, color2.getGreen, color2.getBlue, color2.getAlpha)
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

    def drawObject(xPos: Int, yPos: Int, scale: Float, mesh: Mesh, renderer: EntityRenderer): Unit = {
        if (mesh ne null) {
            EntityRenderer.currentShaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos, yPos, scale))
            EntityRenderer.currentShaderProgram.setUniform("colour", new Vec3f(1f, 1f, 1f))
            mesh.render(renderer.textureManager)
        }
    }

    def drawObject(xPos: Int, yPos: Int, mesh: Mesh, renderer: EntityRenderer): Unit = drawObject(xPos, yPos, 1, mesh, renderer)


}
