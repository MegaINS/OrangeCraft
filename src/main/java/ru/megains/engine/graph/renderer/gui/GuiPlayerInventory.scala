package ru.megains.engine.graph.renderer.gui

import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.game.OrangeCraft
import ru.megains.game.entity.player.EntityPlayer

class GuiPlayerInventory(entityPlayer: EntityPlayer) extends GuiContainer(entityPlayer.inventoryContainer) {


    var playerInventory: Mesh = _

    override def init(orangeCraft: OrangeCraft): Unit = {
        super.init(orangeCraft)
        playerInventory = createTextureRect(500,240,"gui/playerInventory")
    }


    override def drawScreen(mouseX:Int,mouseY:Int): Unit = {
        drawObject(playerInventory,150,0)
        super.drawScreen(mouseX,mouseY)
    }

    override def cleanup(): Unit = {
        playerInventory.cleanUp()
    }
}
