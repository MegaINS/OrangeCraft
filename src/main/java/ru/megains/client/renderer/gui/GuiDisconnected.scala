package ru.megains.client.renderer.gui

import java.awt.Color

import ru.megains.client.OrangeCraft

class GuiDisconnected(screen: GuiScreen, reasonLocalizationKey: String, chatComp: String) extends GuiScreen with GuiText {


    override def initGui(orangeCraft: OrangeCraft): Unit = {
        addText("text", createString(chatComp, Color.BLACK))
        buttonList += new GuiButton(0, oc, "Cancel", 300, 200, 200, 50)
    }

    override def actionPerformed(button: GuiButton): Unit = {
        button.id match {
            case 0 => oc.guiManager.setGuiScreen(screen)
            case _ =>
        }
    }


    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        super.drawScreen(mouseX, mouseY)
        drawObject(300, 300, 2, text("text"))
    }
}
