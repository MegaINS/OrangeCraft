package ru.megains.client.renderer.gui

import org.lwjgl.glfw.GLFW._
import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.mesh.Mesh
import ru.megains.common.entity.player.EntityPlayer

class GuiPlayerInventory(entityPlayer: EntityPlayer) extends GuiContainer(entityPlayer.inventoryContainer) {


    var playerInventory: Mesh = _

    override def initGui(orangeCraft: OrangeCraft): Unit = {
        playerInventory = createTextureRect(500, 240, "gui/playerInventory")
    }


    override def keyTyped(typedChar: Char, keyCode: Int): Unit = {
        keyCode match {
            case GLFW_KEY_E | GLFW_KEY_ESCAPE => oc.guiManager.setGuiScreen(null)
            case _ =>
        }

    }


    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        drawObject(playerInventory, 150, 0)
        super.drawScreen(mouseX, mouseY)
    }

    override def cleanup(): Unit = {
        playerInventory.cleanUp()
    }
}
