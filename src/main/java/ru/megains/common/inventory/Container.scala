package ru.megains.common.inventory

import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.item.ItemStack
import ru.megains.server.entity.EntityPlayerMP

import scala.collection.mutable.ArrayBuffer

abstract class Container {

    val inventorySlots: ArrayBuffer[Slot] = new ArrayBuffer[Slot]()

    def addSlotToContainer(slot: Slot): Unit = {

        slot.slotNumber = inventorySlots.size
        inventorySlots += slot
    }

    def mouseClicked(x: Int, y: Int, button: Int, player: EntityPlayer): Unit = {
        val slot = getSlotAtPosition(x, y)
        if (slot != null) {
            slotClick(slot.slotNumber, button, player)
        }
    }

    def slotClick(slotId: Int, button: Int, player: EntityPlayer): Unit = {

        val slot = inventorySlots(slotId)
        val inventoryPlayer = player.inventory

        button match {
            case 0 =>
                if (inventoryPlayer.itemStack == null) {
                    if (!slot.isEmpty) {
                        inventoryPlayer.itemStack = slot.getStack
                        slot.putStack(null)
                    }
                } else {
                    if (slot.isEmpty) {
                        slot.putStack(inventoryPlayer.itemStack)
                        inventoryPlayer.itemStack = null
                    } else if (slot.getStack.item == inventoryPlayer.itemStack.item) {
                        slot.getStack.stackSize += inventoryPlayer.itemStack.stackSize
                        inventoryPlayer.itemStack = null
                    }
                }
            case 1 =>
                if (inventoryPlayer.itemStack ne null) {
                    if (slot.isEmpty) {
                        slot.putStack(inventoryPlayer.itemStack.splitStack(1))
                    } else if (slot.getStack.item == inventoryPlayer.itemStack.item) {
                        slot.getStack.stackSize += 1
                        inventoryPlayer.itemStack.stackSize -= 1
                    }
                    if (inventoryPlayer.itemStack.stackSize < 1) {
                        inventoryPlayer.itemStack = null
                    }
                } else {
                    if (!slot.isEmpty) {

                        val size: Int = Math.ceil(slot.getStack.stackSize / 2.0).toInt
                        inventoryPlayer.itemStack = slot.decrStackSize(size)
                    }
                }
            case _ =>
        }

    }

    val listeners: ArrayBuffer[EntityPlayerMP] = ArrayBuffer[EntityPlayerMP]()

    def addListener(listener: EntityPlayerMP) {
        if (listeners.contains(listener)) throw new IllegalArgumentException("Listener already listening")
        else {
            listeners += listener
            listener.updateCraftingInventory(this, getInventory)
            detectAndSendChanges()
        }
    }

    def getInventory: Array[ItemStack] = {
        val array = ArrayBuffer[ItemStack]()
        inventorySlots.foreach(array += _.getStack)
        array.toArray
    }

    def detectAndSendChanges() {

        for (i <- inventorySlots.indices) {
            val itemStack = inventorySlots(i).getStack
            listeners.foreach(_.sendSlotContents(this, i, itemStack))
        }

    }

    def putStackInSlot(slot: Int, item: ItemStack): Unit = {
        if (slot < inventorySlots.length) {
            inventorySlots(slot).putStack(item)
        }
    }

    def isMouseOverSlot(slot: Slot, mouseX: Int, mouseY: Int): Boolean = {
        mouseX >= slot.xPos && mouseX <= slot.xPos + 40 && mouseY >= slot.yPos && mouseY <= slot.yPos + 40
    }

    def getSlotAtPosition(x: Int, y: Int): Slot = inventorySlots.find(isMouseOverSlot(_, x, y)).orNull
}
