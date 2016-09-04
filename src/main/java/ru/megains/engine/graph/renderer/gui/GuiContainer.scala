package ru.megains.engine.graph.renderer.gui

import java.awt.Color

import ru.megains.game.inventory.{Container, Slot}

abstract class GuiContainer(inventorySlots: Container) extends GuiScreen{


    def rect = createRect(40,40,new Color(200,255,100,100))


    override def init(): Unit = {}



    override def drawScreen(mouseX:Int, mouseY:Int): Unit = {


        inventorySlots.inventorySlots.foreach(
            (slot)=> {
                drawSlot(slot)
                if(isMouseOverSlot(slot,mouseX, mouseY)){
                    drawObject(rect,slot.xPos,slot.yPos)
                }
            }
        )


    }

    override def cleanup(): Unit = {}

    def isMouseOverSlot(slot: Slot,mouseX:Int, mouseY:Int): Boolean = {
            mouseX>=slot.xPos && mouseX<= slot.xPos +40 && mouseY >= slot.yPos && mouseY<= slot.yPos +40
    }

    def drawSlot(slot: Slot): Unit ={
        drawItemStack(slot.getStack,slot.xPos,slot.yPos)
    }
}
