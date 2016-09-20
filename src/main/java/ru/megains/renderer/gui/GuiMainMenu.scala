package ru.megains.renderer.gui

import ru.megains.game.OrangeCraft
import ru.megains.game.world.World

class GuiMainMenu extends GuiScreen {


    override def init(orangeCraft: OrangeCraft): Unit = {
        super.init(orangeCraft)

        buttonList += new GuiButton(0, orangeCraft, "Load world", 250, 450, 300, 50)
        buttonList += new GuiButton(1, orangeCraft, "Option", 250, 380, 300, 50)
        buttonList += new GuiButton(2, orangeCraft, "Exit game", 250, 310, 300, 50)

    }

    override def actionPerformed(button: GuiButton): Unit = {
        button.id match {
            case 0 => oc.setWorld(new World)
                oc.guiManager.setGuiScreen(null)
            case 2 => oc.running = false
            case _ =>
        }
    }

    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY)
    }

}
