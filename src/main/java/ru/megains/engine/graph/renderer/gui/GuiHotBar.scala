package ru.megains.engine.graph.renderer.gui

import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.game.OrangeCraft

class GuiHotBar(orangeCraft: OrangeCraft) extends GuiInGame(orangeCraft) {

    var hotBar: Mesh = _
    var stackSelect: Mesh = _

    override def init(): Unit = {
        stackSelect = createTextureRect(56, 54, "gui/stackSelect")
        hotBar = createTextureRect(484, 52, "gui/hotBar")
    }

    override def render(): Unit = {

        val inventory = orangeCraft.player.inventory

        renderObject(158, 0, hotBar)
        renderObject(156 + inventory.stackSelect * 48, 0, stackSelect)
        for (i <- 0 to 9) {
            renderItemStack(184 + i * 48, 26, inventory.getStackForIndex(i))
        }


    }

    override def cleanup(): Unit = {
        hotBar.cleanUp()
    }
}
