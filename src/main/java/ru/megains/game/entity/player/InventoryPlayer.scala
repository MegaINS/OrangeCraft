package ru.megains.game.entity.player

import ru.megains.game.item.ItemStack

class InventoryPlayer(val entityPlayer: EntityPlayer) {

    val mainInventory: Array[ItemStack] = new Array[ItemStack](40)
    var stackSelect: Int = 0

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

    def getStackForIndex(index: Int): ItemStack = mainInventory(index)
}
