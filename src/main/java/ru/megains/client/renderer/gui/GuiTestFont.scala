package ru.megains.client.renderer.gui

import java.awt.Color

import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.font.Fonts
import ru.megains.client.renderer.gui.Element.GuiButton
import ru.megains.client.renderer.mesh.MeshMaker

class GuiTestFont extends GuiScreen with GuiText {

    val mm = MeshMaker
    mm.startMakeTriangles()
    mm.setTexture(Fonts.timesNewRomanR.name)
    mm.addColor(Color.WHITE)
    mm.setCurrentIndex()
    mm.addVertexWithUV(0, -1000, 0, 0, 1)
    mm.addVertexWithUV(0, 0, 0, 0, 0)
    mm.addVertexWithUV(800, 0, 0, 1, 0)
    mm.addVertexWithUV(800, -1000, 0, 1, 1)
    mm.addIndex(1, 0, 3)
    mm.addIndex(2, 1, 3)

    val all = mm.makeMesh()

    override def initGui(orangeCraft: OrangeCraft): Unit = {

        addText("1", createString("Привет", Color.WHITE))


    }

    override def actionPerformed(button: GuiButton): Unit = {
        button.id match {
            case _ => oc.playerName = button.buttonText
                oc.guiManager.setGuiScreen(new GuiPlayerSelect())
        }
    }

    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY)

        drawObject(0, 50 + 100, 1, text("1"))

        //  drawObject(0,600, 1,  all)

    }
}
