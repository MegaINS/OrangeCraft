package ru.megains.common.inventory

import ru.megains.common.item.ItemStack
import ru.megains.nbt.tag.NBTCompound

abstract class AInventory {


    def getStackInSlot(index: Int): ItemStack

    def setInventorySlotContents(index: Int, itemStack: ItemStack): Unit


    def decrStackSize(index: Int, size: Int): ItemStack

    def writeToNBT(data: NBTCompound): Unit

    def readFromNBT(data: NBTCompound): Unit



}
