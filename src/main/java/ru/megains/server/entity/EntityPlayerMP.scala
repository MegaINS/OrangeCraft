package ru.megains.server.entity

import ru.megains.common.block.Block
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.inventory.Container
import ru.megains.common.item.ItemStack
import ru.megains.common.network.NetHandlerPlayServer
import ru.megains.common.network.play.server.{SPacketSetSlot, SPacketWindowItems}
import ru.megains.common.register.Items
import ru.megains.common.world.World
import ru.megains.server.world.WorldServer
import ru.megains.server.{OrangeCraftServer, PlayerInteractionManager}

import scala.util.Random

class EntityPlayerMP(name: String, world: World, val interactionManager: PlayerInteractionManager) extends EntityPlayer(name: String, world) {



    interactionManager.thisPlayerMP = this
    var connection: NetHandlerPlayServer = _
    var managedPosZ: Double = .0
    var managedPosY: Double = .0
    var managedPosX: Double = .0

    var playerLastActiveTime: Long = 0

    inventory.addItemStackToInventory(new ItemStack(Items.stick, 10))

    val rand: Int = Random.nextInt(15)
    for (id <- 0 to rand) {
        inventory.addItemStackToInventory(new ItemStack(Block.getBlockById(2 + Random.nextInt(10)), id))
    }

    //
    //    for (id <- 2 to 13) {
    //        inventory.addItemStackToInventory(new ItemStack(Block.getBlockById(id), id))
    //    }

    def getWorldServer: WorldServer = world.asInstanceOf[WorldServer]


    def markPlayerActive() {
        playerLastActiveTime = OrangeCraftServer.getCurrentTimeMillis
    }

    def addSelfToInternalCraftingInventory() {
        openContainer.addListener(this)
    }

    def sendSlotContents(containerToSend: Container, slotInd: Int, stack: ItemStack) {
        connection.sendPacket(new SPacketSetSlot(-1, slotInd, stack))
    }

    def updateCraftingInventory(containerToSend: Container, itemsList: Array[ItemStack]) {
        connection.sendPacket(new SPacketWindowItems(-1, itemsList))
        updateHeldItem()
    }

    def updateHeldItem() {
        connection.sendPacket(new SPacketSetSlot(-1, -1, inventory.itemStack))
    }

    override def update(): Unit = {
        super.update()
        openContainer.detectAndSendChanges()
    }
}
