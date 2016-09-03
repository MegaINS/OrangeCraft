package ru.megains.engine.graph.renderer.gui

import java.awt.Color

import ru.megains.engine.graph.Renderer
import ru.megains.engine.graph.renderer.ItemRender
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.game.OrangeCraft
import ru.megains.game.item.ItemStack

abstract class GuiScreen(orangeCraft: OrangeCraft)  {

    val itemRender:ItemRender = orangeCraft.itemRender
    val renderer:Renderer = orangeCraft.renderer

    def createRect(width: Int, height: Int, color: Color): Mesh = Gui.createRect(width,height,color)

    def createTextureRect(width: Int, height: Int, texture: String): Mesh = Gui.createTextureRect(width, height, texture)

    def renderObject(xPos:Int,yPos:Int,mesh: Mesh): Unit = Gui.renderObject(xPos,yPos,mesh,renderer)

    def renderItemStack(xPos:Int,yPos:Int,itemStack: ItemStack): Unit = itemRender.renderItemStackToGui(xPos,yPos,itemStack)


    def init(): Unit

    def render(): Unit

    def cleanup(): Unit

}
