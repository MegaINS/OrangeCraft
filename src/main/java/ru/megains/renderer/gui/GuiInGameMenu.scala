package ru.megains.renderer.gui

import java.awt.Color

import ru.megains.game.OrangeCraft
import ru.megains.renderer.mesh.Mesh

class GuiInGameMenu extends GuiScreen {

    var background: Mesh = _

    override def init(orangeCraft: OrangeCraft): Unit = {
        super.init(orangeCraft)
        background = createGradientRect(800, 600, new Color(128, 128, 128, 128), new Color(0, 0, 0, 128))
        buttonList += new GuiButton(0, orangeCraft, "Hello", 100, 100, 200, 50)


    }


    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        drawObject(background, 0, 0)
        super.drawScreen(mouseX, mouseY)
    }


    override def cleanup(): Unit = {
        super.cleanup()
    }
}
