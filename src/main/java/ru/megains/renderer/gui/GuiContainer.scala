package ru.megains.renderer.gui

import java.awt.Color

import ru.megains.game.inventory.{Container, Slot}

abstract class GuiContainer(inventorySlots: Container) extends GuiScreen {


    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {

        inventorySlots.inventorySlots.foreach(
            (slot) => {
                drawSlot(slot)
                if (isMouseOverSlot(slot, mouseX, mouseY)) {
                    drawObject(rect, slot.xPos, slot.yPos)
                }
            }
        )
        drawItemStack(inventorySlots.stackSelect, mouseX - 20, mouseY - 15)


    }

    def rect = createRect(40, 40, new Color(200, 255, 100, 100))

    def isMouseOverSlot(slot: Slot, mouseX: Int, mouseY: Int): Boolean = {
        mouseX >= slot.xPos && mouseX <= slot.xPos + 40 && mouseY >= slot.yPos && mouseY <= slot.yPos + 40
    }

    def drawSlot(slot: Slot): Unit = {
        drawItemStack(slot.getStack, slot.xPos, slot.yPos)
    }

    override def cleanup(): Unit = {}

    override def mouseClicked(x: Int, y: Int, button: Int): Unit = {
        val slot = getSlotAtPosition(x, y)
        if (slot != null) {
            inventorySlots.slotClick(slot.slotNumber, button)
        }

    }

    def getSlotAtPosition(x: Int, y: Int): Slot = inventorySlots.inventorySlots.find(isMouseOverSlot(_, x, y)).orNull
}
