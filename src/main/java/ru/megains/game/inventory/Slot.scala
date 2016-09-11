package ru.megains.game.inventory

import ru.megains.game.item.ItemStack

class Slot(inventory: AInventory, val index: Int, val xPos: Int, val yPos: Int) {


    def isEmpty: Boolean = getStack == null

    def getStack: ItemStack = inventory.getStackInSlot(index)

    def putStack(itemStack: ItemStack): Unit = {
        inventory.setInventorySlotContents(index, itemStack)
    }
}
