package ru.megains.renderer.gui

import java.awt.Color

import ru.megains.game.OrangeCraft
import ru.megains.renderer.mesh.Mesh

class GuiButton(val id: Int, oc: OrangeCraft, buttonText: String, positionX: Int, positionY: Int, weight: Int, height: Int) extends Gui {

    val textMesh: Mesh = createString(oc.fontRender, buttonText, Color.WHITE)
    val buttonUp = createRect(weight, height, Color.blue)
    val buttonDown = createRect(weight, height, Color.darkGray)


    def draw(mouseX: Int, mouseY: Int): Unit = {
        val background: Mesh = if (isMouseOver(mouseX, mouseY)) buttonDown else buttonUp

        drawObject(positionX, positionX, 1, background, oc.renderer)

        drawObject(positionX, positionX, 2, textMesh, oc.renderer)

    }

    def clear(): Unit = {
        textMesh.cleanUp()
        buttonDown.cleanUp()
        buttonUp.cleanUp()
    }

    def isMouseOver(mouseX: Int, mouseY: Int): Boolean = {
        mouseX >= positionX && mouseX <= positionX + weight && mouseY >= positionY && mouseY <= positionY + height
    }


}
