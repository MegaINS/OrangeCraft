package ru.megains.game.inventory

import ru.megains.game.item.ItemStack

abstract class AInventory {
    def getStackInSlot(index: Int): ItemStack

}
