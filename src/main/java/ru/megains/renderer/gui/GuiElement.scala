package ru.megains.renderer.gui

import java.awt.Color

import ru.megains.game.OrangeCraft
import ru.megains.game.item.ItemStack
import ru.megains.renderer.mesh.Mesh
import ru.megains.renderer.{EntityRenderer, FontRender, RenderItem}

abstract class GuiElement extends Gui {

    def this(orangeCraft: OrangeCraft) {
        this()
        setData(orangeCraft)
    }

    var itemRender: RenderItem = _
    var renderer: EntityRenderer = _
    var fontRender: FontRender = _
    var oc: OrangeCraft = _


    def setData(orangeCraft: OrangeCraft): Unit = {
        itemRender = orangeCraft.itemRender
        renderer = orangeCraft.renderer
        fontRender = orangeCraft.fontRender
        oc = orangeCraft

        initGui(orangeCraft)
    }

    def initGui(orangeCraft: OrangeCraft): Unit = {}

    def drawObject(mesh: Mesh, xPos: Int, yPos: Int): Unit = super.drawObject(xPos, yPos, 1, mesh, renderer)

    def drawObject(xPos: Int, yPos: Int, scale: Float, mesh: Mesh): Unit = super.drawObject(xPos, yPos, scale, mesh, renderer)

    def createString(text: String, color: Color): Mesh = fontRender.createStringGui(text, color)

    def drawItemStack(itemStack: ItemStack, xPos: Int, yPos: Int): Unit = itemRender.renderItemStackToGui(xPos, yPos, itemStack)
}
