package ru.megains.common.inventory

import ru.megains.common.item.ItemStack

import scala.collection.mutable.ArrayBuffer

abstract class Container {

    val inventorySlots: ArrayBuffer[Slot] = new ArrayBuffer[Slot]()
    var stackSelect: ItemStack = _


    def addSlotToContainer(slot: Slot): Unit = {

        slot.slotNumber = inventorySlots.size
        inventorySlots += slot
    }


    def slotClick(slotId: Int, button: Int): Unit = {

        val slot = inventorySlots(slotId)


        button match {
            case 0 =>
                if (stackSelect == null) {
                    if (!slot.isEmpty) {
                        stackSelect = slot.getStack
                        slot.putStack(null)
                    }
                } else {
                    if (slot.isEmpty) {
                        slot.putStack(stackSelect)
                        stackSelect = null
                    } else if (slot.getStack.item == stackSelect.item) {
                        slot.getStack.stackSize += stackSelect.stackSize
                        stackSelect = null
                    }
                }
            case 1 =>
                if (stackSelect ne null) {
                    if (slot.isEmpty) {
                        slot.putStack(stackSelect.splitStack(1))
                    } else if (slot.getStack.item == stackSelect.item) {
                        slot.getStack.stackSize += 1
                        stackSelect.stackSize -= 1
                    }
                    if (stackSelect.stackSize < 1) {
                        stackSelect = null
                    }
                } else {
                    if (!slot.isEmpty) {

                        val size: Int = Math.ceil(slot.getStack.stackSize / 2.0).toInt
                        stackSelect = slot.decrStackSize(size)
                    }
                }
            case _ =>
        }

    }
}
