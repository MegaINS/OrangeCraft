package ru.megains.game.inventory

import ru.megains.game.item.ItemStack

class Slot(inventory: AInventory,val index:Int,val xPos:Int,val yPos:Int) {


    def getStack: ItemStack = inventory.getStackInSlot(index)

}
