package ru.megains.server.entity

import ru.megains.common.network.NetHandlerPlayServer
import ru.megains.game.entity.player.EntityPlayer
import ru.megains.game.world.World
import ru.megains.server.world.WorldServer
import ru.megains.server.{OrangeCraftServer, PlayerInteractionManager}

class EntityPlayerMP(name: String, world: World, val interactionManager: PlayerInteractionManager) extends EntityPlayer(name: String, world) {


    interactionManager.thisPlayerMP = this
    var connection: NetHandlerPlayServer = _

    var managedPosZ: Double = .0
    var managedPosY: Double = .0
    var managedPosX: Double = .0

    var playerLastActiveTime: Long = 0

    def getWorldServer: WorldServer = world.asInstanceOf[WorldServer]


    def markPlayerActive() {
        playerLastActiveTime = OrangeCraftServer.getCurrentTimeMillis
    }
}
