package ru.megains.game.entity.player

import ru.megains.game.inventory.AInventory
import ru.megains.game.item.ItemStack

class InventoryPlayer(val entityPlayer: EntityPlayer) extends AInventory {

    val mainInventory: Array[ItemStack] = new Array[ItemStack](40)
    var stackSelect: Int = 0


    def changeStackSelect(value: Int): Unit = {
        var offset: Int = 0
        if (value > 0) {
            offset = 1
        }
        if (value < 0) {
            offset = -1
        }
        stackSelect += offset

        if (stackSelect > 9) {
            stackSelect = 0
        }
        if (stackSelect < 0) {
            stackSelect = 9
        }
    }

    def addItemStackToInventory(itemStack: ItemStack): Boolean = {

        val index = getEmptyStack
        if (index != -1) {
            mainInventory(index) = itemStack
            true
        } else {
            false
        }


    }


    def getEmptyStack: Int = mainInventory.indexOf(null)

    def getStackSelect: ItemStack = mainInventory(stackSelect)


    override def getStackInSlot(index: Int): ItemStack = mainInventory(index)

    override def setInventorySlotContents(index: Int, itemStack: ItemStack): Unit = {
        mainInventory(index) = itemStack
    }

    def decrStackSize(index: Int, size: Int): ItemStack = {
        val stack = mainInventory(index)
        var newStack: ItemStack = null
        if (stack ne null) {
            if (stack.stackSize <= size) {
                newStack = stack
                mainInventory(index) = null
            } else {
                newStack = stack.splitStack(size)
                if (stack.stackSize < 1) {
                    mainInventory(index) = null
                }
            }
        }
        newStack
    }
}

object InventoryPlayer {
    def isHotBar(index: Int): Boolean = index > -1 && index < hotBarSize

    val hotBarSize = 10


}
