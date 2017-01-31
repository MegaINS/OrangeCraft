package ru.megains.server.entity

import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.inventory.Container
import ru.megains.common.item.ItemStack
import ru.megains.common.network.NetHandlerPlayServer
import ru.megains.common.network.play.server.{SPacketChangeGameState, SPacketSetSlot, SPacketWindowItems}
import ru.megains.common.world.{GameType, World}
import ru.megains.server.world.WorldServer
import ru.megains.server.{OrangeCraftServer, PlayerInteractionManager}

class EntityPlayerMP(name: String, world: World, val interactionManager: PlayerInteractionManager) extends EntityPlayer(name: String, world) {


    val ocServer = getWorldServer.server
    interactionManager.thisPlayerMP = this
    var connection: NetHandlerPlayServer = _
    var managedPosZ: Double = .0
    var managedPosY: Double = .0
    var managedPosX: Double = .0

    var playerLastActiveTime: Long = 0

    def setGameType(gameType: GameType) {
        interactionManager.setGameType(gameType)
        this.connection.sendPacket(new SPacketChangeGameState(3, gameType.id))
        //    if (gameType eq GameType.SPECTATOR) this.dismountRidingEntity()
        //  else this.setSpectatingEntity(this)
        //  this.sendPlayerAbilities()
        //  this.markPotionsDirty()
    }


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
