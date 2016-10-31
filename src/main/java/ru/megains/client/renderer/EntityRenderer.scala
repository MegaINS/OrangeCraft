package ru.megains.client.renderer

import java.nio.FloatBuffer

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11._
import ru.megains.client.renderer.graph.{Camera, ShaderProgram, Transformation}
import ru.megains.client.renderer.world.{RenderChunk, WorldRenderer}
import ru.megains.game.OrangeCraft
import ru.megains.game.managers.TextureManager
import ru.megains.game.util.Utils


class EntityRenderer(oc: OrangeCraft) {
    val transformation = new Transformation
    var textureManager: TextureManager = _
    var hudShaderProgram: ShaderProgram = _
    var sceneShaderProgram: ShaderProgram = _
    var frustum: Frustum = _
    var worldRenderer: WorldRenderer = _
    val FOV: Float = Math.toRadians(60.0f).toFloat
    val Z_NEAR: Float = 0.01f
    val Z_FAR: Float = 1000f


    @throws[Exception]
    def init(textureManager: TextureManager) {
        this.textureManager = textureManager
        setupSceneShader()
        setupHudShader()
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    @throws[Exception]
    private def setupSceneShader() {
        sceneShaderProgram = new ShaderProgram
        sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"))
        sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"))
        sceneShaderProgram.link()

        sceneShaderProgram.createUniform("projectionMatrix")
        sceneShaderProgram.createUniform("modelViewMatrix")
    }

    @throws[Exception]
    private def setupHudShader() {
        hudShaderProgram = new ShaderProgram
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud_vertex.vs"))
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud_fragment.fs"))
        hudShaderProgram.link()

        hudShaderProgram.createUniform("projectionMatrix")
        hudShaderProgram.createUniform("modelMatrix")
        hudShaderProgram.createUniform("colour")
        hudShaderProgram.createUniform("useTexture")
    }


    def bindShaderProgram(shaderProgram: ShaderProgram): Unit = {
        EntityRenderer.bindShaderProgram(shaderProgram)
    }

    def unbindShaderProgram(): Unit = {
        EntityRenderer.unbindShaderProgram()
    }

    def render(camera: Camera) {
        transformation.updateProjectionMatrix(FOV, 1600, 1200, Z_NEAR, Z_FAR, camera)
        transformation.updateViewMatrix(camera)
        val projectionMatrix: Matrix4f = transformation.getProjectionMatrix
        val viewMatrix: Matrix4f = transformation.getViewMatrix
        val _proj: FloatBuffer = projectionMatrix.get(BufferUtils.createFloatBuffer(16))
        projectionMatrix.mul(viewMatrix)
        val _modl: FloatBuffer = viewMatrix.get(BufferUtils.createFloatBuffer(16))
        frustum = Frustum.getFrustum(_proj, _modl)


        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        if (oc.world != null) renderScene()
        renderGui()
        Display.update()
    }

    private def renderScene() {


        bindShaderProgram(sceneShaderProgram)

        val projectionMatrix: Matrix4f = transformation.getProjectionMatrix
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix)

        glEnable(GL_CULL_FACE)

        var modelViewMatrix: Matrix4f = null
        RenderChunk.clearRend()


        worldRenderer.getRenderChunks(oc.player, frustum).foreach((renderChunk: RenderChunk) => {
            sceneShaderProgram.setUniform("modelViewMatrix", transformation.buildChunkModelViewMatrix(renderChunk.chunk.position))
            renderChunk.render(0)
        }


        )

        worldRenderer.renderEntitiesItem(frustum, transformation)
        glDisable(GL_CULL_FACE)



        val objectMouseOver = oc.objectMouseOver
        if (objectMouseOver != null) {
            modelViewMatrix = transformation.buildBlockModelViewMatrix(objectMouseOver.blockPos)
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            worldRenderer.renderBlockMouseOver()
        }

        val blockSelectPosition = oc.blockSelectPosition
        if (blockSelectPosition != null) {
            modelViewMatrix = transformation.buildBlockModelViewMatrix(blockSelectPosition)
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            worldRenderer.renderBlockSelect()
        }
        unbindShaderProgram()
    }


    private def renderGui() {
        glEnable(GL_BLEND)
        glEnable(GL_CULL_FACE)
        glDisable(GL_DEPTH_TEST)

        bindShaderProgram(hudShaderProgram)


        val ortho: Matrix4f = transformation.getOrtho2DProjectionMatrix(0, 800, 0, 600)
        hudShaderProgram.setUniform("projectionMatrix", ortho)
        oc.guiManager.draw(Mouse.getX, Mouse.getY)

        unbindShaderProgram()


        glEnable(GL_DEPTH_TEST)
        glDisable(GL_BLEND)
        glDisable(GL_CULL_FACE)
    }

    def cleanup() {
        if (sceneShaderProgram != null) sceneShaderProgram.cleanup()
        if (hudShaderProgram != null) hudShaderProgram.cleanup()
    }
}

object EntityRenderer {
    var currentShaderProgram: ShaderProgram = _

    def bindShaderProgram(shaderProgram: ShaderProgram): Unit = {
        if (currentShaderProgram ne null) currentShaderProgram.unbind()
        currentShaderProgram = shaderProgram
        currentShaderProgram.bind()
    }

    def unbindShaderProgram(): Unit = {
        if (currentShaderProgram ne null) currentShaderProgram.unbind()
        currentShaderProgram = null
    }
}

