package ru.megains.common.entity


import ru.megains.common.item.ItemStack
import ru.megains.common.world.World

abstract class EntityLivingBase(world: World, height: Float, wight: Float, levelView: Float) extends Entity(world, height, wight, levelView) {


    def setHeldItem(stack: ItemStack): Unit = setItemStackToSlot(stack)


    def getHeldItem: ItemStack = getItemStackFromSlot


    def getItemStackFromSlot: ItemStack

    def setItemStackToSlot(stack: ItemStack): Unit
}
