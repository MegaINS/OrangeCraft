package ru.megains.game.entity.player

import ru.megains.game.item.ItemStack

class InventoryPlayer(val entityPlayer: EntityPlayer) {

    val mainInventory: Array[ItemStack] = new Array[ItemStack](40)
    var stackSelect: Int = 0


    def changeStackSelect(value:Int): Unit ={
        var offset:Int = 0
        if(value > 0){
            offset = 1
        }
        if(value < 0){
            offset = -1
        }
        stackSelect += offset

        if(stackSelect > 9){
            stackSelect = 0
        }
        if(stackSelect < 0){
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

    def getStackForIndex(index: Int): ItemStack = mainInventory(index)
}
