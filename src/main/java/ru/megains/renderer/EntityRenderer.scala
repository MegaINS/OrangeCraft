package ru.megains.renderer

import java.nio.FloatBuffer

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11._
import ru.megains.engine.graph.{Camera, ShaderProgram, Transformation}
import ru.megains.game.OrangeCraft
import ru.megains.game.managers.TextureManager
import ru.megains.game.util.{BlockAndPos, Utils}
import ru.megains.renderer.world.{RenderChunk, WorldRenderer}


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
        hudShaderProgram.createUniform("hasTexture")
    }

    def render(camera: Camera) {
        transformation.updateProjectionMatrix(FOV, 800, 600, Z_NEAR, Z_FAR, camera)
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
        sceneShaderProgram.bind()
        val projectionMatrix: Matrix4f = transformation.getProjectionMatrix
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix)

        glEnable(GL_CULL_FACE)

        var modelViewMatrix: Matrix4f = null
        RenderChunk.clearRend()


        worldRenderer.getRenderChunks(oc.player, frustum).foreach((renderChunk: RenderChunk) => {
            sceneShaderProgram.setUniform("modelViewMatrix", transformation.buildChunkModelViewMatrix(renderChunk.chunk.position))
            renderChunk.render(0, sceneShaderProgram)
        }


        )

        worldRenderer.renderEntitiesItem(frustum, transformation, sceneShaderProgram)
        glDisable(GL_CULL_FACE)



        val bp: BlockAndPos = oc.blockAndPos
        if (bp != null) {
            modelViewMatrix = transformation.buildBlockModelViewMatrix(bp.pos)
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            worldRenderer.renderBlockBounds(sceneShaderProgram)
        }
        sceneShaderProgram.unbind()
    }


    private def renderGui() {
        glEnable(GL_BLEND)
        glEnable(GL_CULL_FACE)
        glDisable(GL_DEPTH_TEST)

        hudShaderProgram.bind()

        val ortho: Matrix4f = transformation.getOrtho2DProjectionMatrix(0, 800, 0, 600)
        hudShaderProgram.setUniform("projectionMatrix", ortho)
        oc.guiManager.draw(Mouse.getX, Mouse.getY)

        hudShaderProgram.unbind()

        glEnable(GL_DEPTH_TEST)
        glDisable(GL_BLEND)
        glDisable(GL_CULL_FACE)
    }

    def cleanup() {
        if (sceneShaderProgram != null) sceneShaderProgram.cleanup()
        if (hudShaderProgram != null) hudShaderProgram.cleanup()
    }
}
