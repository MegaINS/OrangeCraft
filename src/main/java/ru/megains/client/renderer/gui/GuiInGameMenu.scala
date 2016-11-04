package ru.megains.client.renderer.gui

import ru.megains.client.OrangeCraft

class GuiInGameMenu extends GuiScreen {


    override def initGui(orangeCraft: OrangeCraft): Unit = {
        buttonList += new GuiButton(0, orangeCraft, "Main menu", 250, 450, 300, 50)
        buttonList += new GuiButton(1, orangeCraft, "Option", 250, 380, 300, 50)
        buttonList += new GuiButton(2, orangeCraft, "Return to game", 250, 310, 300, 50)

    }

    override def actionPerformed(button: GuiButton): Unit = {
        button.id match {

            case 0 => oc.loadWorld(null)
                oc.guiManager.setGuiScreen(new GuiMainMenu())

            case 1 => oc.guiManager.setGuiScreen(null)
            case 2 => oc.guiManager.setGuiScreen(null)
            case _ =>
        }
    }

    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY)
    }


}
