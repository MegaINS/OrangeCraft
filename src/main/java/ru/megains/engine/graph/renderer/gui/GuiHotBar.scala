package ru.megains.engine.graph.renderer.gui

import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.game.OrangeCraft

class GuiHotBar(orangeCraft: OrangeCraft) extends GuiInGame() {

    var hotBar: Mesh = _
    var stackSelect: Mesh = _

    override def init(): Unit = {
        stackSelect = createTextureRect(56, 54, "gui/stackSelect")
        hotBar = createTextureRect(484, 52, "gui/hotBar")
    }

    override def drawScreen(mouseX:Int,mouseY:Int): Unit = {

        val inventory = orangeCraft.player.inventory

        drawObject(hotBar,158, 0)
        drawObject(stackSelect,156 + inventory.stackSelect * 48, 0 )
        for (i <- 0 to 9) {
            drawItemStack( inventory.getStackInSlot(i),184 + i * 48, 26)
        }


    }

    override def cleanup(): Unit = {
        hotBar.cleanUp()
    }
}
