package ru.megains.client.renderer.gui

import java.awt.Color

import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.gui.Element.GuiElement
import ru.megains.client.renderer.mesh.Mesh

class GuiSlotWorld(id: Int, val worldName: String, orangeCraft: OrangeCraft) extends GuiElement(orangeCraft) {

    val weight: Int = 400
    val height: Int = 80
    val positionX: Int = 100
    val positionY: Int = 500 - 110 * id

    val textMesh: Mesh = createString(worldName, Color.WHITE)
    val slotSelect = createRect(weight, height, Color.darkGray)
    val slot = createRect(weight, height, Color.BLACK)
    var select: Boolean = false


    def draw(mouseX: Int, mouseY: Int): Unit = {

        val background: Mesh = if (select) slotSelect else slot

        drawObject(positionX, positionY, 1, background)

        drawObject(positionX + 10, positionY + 30, 2, textMesh)

    }

    def isMouseOver(mouseX: Int, mouseY: Int): Boolean = {
        mouseX >= positionX && mouseX <= positionX + weight && mouseY >= positionY && mouseY <= positionY + height
    }
}
