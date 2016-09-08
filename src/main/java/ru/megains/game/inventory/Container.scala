package ru.megains.game.inventory

import ru.megains.game.item.ItemStack

import scala.collection.mutable.ArrayBuffer

abstract class Container {

    val inventorySlots:ArrayBuffer[Slot] = new ArrayBuffer[Slot]()
    var stackSelect:ItemStack = _


    def addSlotToContainer(slot: Slot): Unit ={
        inventorySlots += slot
    }
}
