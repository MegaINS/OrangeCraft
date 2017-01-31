package ru.megains.common.entity.player

import ru.megains.common.inventory.AInventory
import ru.megains.common.item.{Item, ItemStack}
import ru.megains.nbt.NBTType._
import ru.megains.nbt.tag.NBTCompound

class InventoryPlayer(val entityPlayer: EntityPlayer) extends AInventory {



    val mainInventory: Array[ItemStack] = new Array[ItemStack](40)
    var stackSelect: Int = 0
    var itemStack: ItemStack = _

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

    def writeToNBT(data: NBTCompound): Unit = {
        val inventory = data.createList("mainInventory", EnumNBTCompound)
        for (i <- mainInventory.indices) {
            val compound = inventory.createCompound()
            val itemStack = mainInventory(i)

            if (itemStack != null) {
                compound.setValue("id", Item.getIdFromItem(itemStack.item))
                compound.setValue("stackSize", itemStack.stackSize)
            } else {
                compound.setValue("id", -1)
            }
        }
    }

    def readFromNBT(data: NBTCompound): Unit = {
        val inventory = data.getList("mainInventory")
        for (i <- mainInventory.indices) {
            val compound = inventory.getCompound(i)
            val id: Int = compound.getInt("id")
            if (id != -1) {
                val itemStack = new ItemStack(Item.getItemById(id), compound.getInt("stackSize"))
                mainInventory(i) = itemStack
            }
        }
    }
}

object InventoryPlayer {
    def isHotBar(index: Int): Boolean = index > -1 && index < hotBarSize

    val hotBarSize = 10


}
