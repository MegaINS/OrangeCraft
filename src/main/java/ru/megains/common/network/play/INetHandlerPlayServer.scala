package ru.megains.common.network.play

import ru.megains.common.network.INetHandler
import ru.megains.common.network.play.client._

trait INetHandlerPlayServer extends INetHandler {


    def processPlayer(player: CPacketPlayer): Unit

    def processPlayerDigging(packetIn: CPacketPlayerDigging): Unit

    def processHeldItemChange(packetIn: CPacketHeldItemChange)

    def processPlayerBlockPlacement(packetIn: CPacketPlayerTryUseItem)

    def processRightClickBlock(packetIn: CPacketPlayerTryUseItemOnBlock)

}
