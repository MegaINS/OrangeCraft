package ru.megains.engine.graph.renderer.gui

import java.awt.Color

import org.joml.{Matrix4f, Vector3f}
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.engine.graph.{Renderer, ShaderProgram}
import ru.megains.game.OrangeCraft
import ru.megains.game.register.GameRegister

class GuiHotBar(orangeCraft: OrangeCraft) extends GuiInGame {

    private[graph] var selectItemPos: GuiRenderInfo = new GuiRenderInfo(300, 40)
    private[graph] var rectPos: GuiRenderInfo = new GuiRenderInfo(200, 0, 0, 0, 0, 1f)
    var rect: Mesh = _
    override def init(): Unit = {
        rect = createRect(400,80,Color.orange)
    }

    override def render(renderer: Renderer): Unit = {

        val inventory = orangeCraft.player.inventory
        var projModelMatrix: Matrix4f = null
        val shaderProgram: ShaderProgram = renderer.hudShaderProgram

        projModelMatrix = renderer.transformation.buildOrtoProjModelMatrix(rectPos)
        shaderProgram.setUniform("modelMatrix", projModelMatrix)
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        rect.render(shaderProgram, renderer.textureManager)

        for (i<-0 to 9){
            val itemStack  = inventory.getStackForIndex(i)
            if (itemStack != null) {
                val selectItemPos: GuiRenderInfo = new GuiRenderInfo(230+i*50, 40)
                projModelMatrix = renderer.transformation.buildOrtoProjModelMatrix(selectItemPos)
                shaderProgram.setUniform("modelMatrix", projModelMatrix)
                shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
                GameRegister.getItemRender(itemStack.item).renderInInventory(shaderProgram, renderer.textureManager)
            }

        }


    }

    override def cleanup(): Unit = ???
}
