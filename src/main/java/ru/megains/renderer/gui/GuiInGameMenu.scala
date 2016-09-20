package ru.megains.renderer.gui

import ru.megains.game.OrangeCraft

class GuiInGameMenu extends GuiScreen {


    override def init(orangeCraft: OrangeCraft): Unit = {
        super.init(orangeCraft)


        buttonList += new GuiButton(0, orangeCraft, "Main menu", 250, 450, 300, 50)
        buttonList += new GuiButton(1, orangeCraft, "Option", 250, 380, 300, 50)
        buttonList += new GuiButton(2, orangeCraft, "Return to game", 250, 310, 300, 50)

    }

    override def actionPerformed(button: GuiButton): Unit = {
        button.id match {
            case 0 => oc.guiManager.setGuiScreen(new GuiMainMenu)
                oc.setWorld(null)
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
