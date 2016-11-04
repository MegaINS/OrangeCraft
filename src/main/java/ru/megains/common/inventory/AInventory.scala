package ru.megains.common.inventory

import ru.megains.common.item.ItemStack

abstract class AInventory {


    def getStackInSlot(index: Int): ItemStack

    def setInventorySlotContents(index: Int, itemStack: ItemStack): Unit


    def decrStackSize(index: Int, size: Int): ItemStack



}
