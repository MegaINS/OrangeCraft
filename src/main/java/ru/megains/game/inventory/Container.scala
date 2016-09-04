package ru.megains.game.inventory

import scala.collection.mutable.ArrayBuffer

abstract class Container {

    val inventorySlots:ArrayBuffer[Slot] = new ArrayBuffer[Slot]()


    def addSlotToContainer(slot: Slot): Unit ={
        inventorySlots += slot
    }
}
