package ru.megains.game.inventory

import ru.megains.game.item.ItemStack

abstract class AInventory {

    def getStackInSlot(index: Int): ItemStack

    def setInventorySlotContents(index: Int, itemStack: ItemStack): Unit

}
