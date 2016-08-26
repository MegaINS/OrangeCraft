package ru.megains.engine.graph.renderer.gui

import org.joml.{Matrix4f, Vector3f}
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.engine.graph.{Renderer, ShaderProgram}
import ru.megains.game.CubeGame
import ru.megains.game.item.Item
import ru.megains.game.register.GameRegister

class GuiInventory extends GuiScreen{
    private[graph] var selectItemPos: GuiRenderInfo = new GuiRenderInfo(240, 40)
    private[graph] var rectPos: GuiRenderInfo = new GuiRenderInfo(200,0,0,0,0,1f)
    var rect:Mesh = null
    var rock:String = "rock"

    override def init(): Unit = {

        rect = createTextureRect(400,80,rock)
    }



    override def render(renderer: Renderer): Unit = {
        val item: Item = Item.getItemById(CubeGame.megaGame.blockSelect)
        var projModelMatrix: Matrix4f = null
        val shaderProgram:ShaderProgram = renderer.hudShaderProgram

        projModelMatrix = renderer.transformation.buildOrtoProjModelMatrix(rectPos)
        shaderProgram.setUniform("modelMatrix", projModelMatrix)
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
     //   shaderProgram.setUniform("hasTexture", 0)
        rect.render(shaderProgram,renderer.textureManager)


        projModelMatrix = renderer.transformation.buildOrtoProjModelMatrix(selectItemPos)
        shaderProgram.setUniform("modelMatrix", projModelMatrix)
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        GameRegister.getItemRender(item).renderInInventory(shaderProgram,renderer.textureManager )

       // CubeGame.megaGame.textureManager.bindTexture(rock)



    }

    override def cleanup(): Unit = {
        rect.cleanUp()
    }
}
