package ru.megains.client.renderer.gui

import java.awt.Color

import ru.megains.client.renderer.mesh.Mesh
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.inventory.{Container, Slot}

abstract class GuiContainer(inventorySlots: Container) extends GuiScreen {

    val rect: Mesh = createRect(40, 40, new Color(200, 255, 100, 100))

    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {

        inventorySlots.inventorySlots.foreach(
            (slot) => {
                drawSlot(slot)
                if (isMouseOverSlot(slot, mouseX, mouseY)) {
                    drawObject(rect, slot.xPos, slot.yPos)
                }
            }
        )

        drawItemStack(oc.player.inventory.itemStack, mouseX - 20, mouseY - 15)


    }



    def isMouseOverSlot(slot: Slot, mouseX: Int, mouseY: Int): Boolean = {
        mouseX >= slot.xPos && mouseX <= slot.xPos + 40 && mouseY >= slot.yPos && mouseY <= slot.yPos + 40
    }

    def drawSlot(slot: Slot): Unit = {
        drawItemStack(slot.getStack, slot.xPos, slot.yPos)
    }

    override def cleanup(): Unit = {}

    override def mouseClicked(x: Int, y: Int, button: Int, player: EntityPlayer): Unit = {
        oc.playerController.windowClick(x, y, button, player: EntityPlayer)
    }

    def getSlotAtPosition(x: Int, y: Int): Slot = inventorySlots.inventorySlots.find(isMouseOverSlot(_, x, y)).orNull
}
