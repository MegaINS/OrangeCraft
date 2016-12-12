package ru.megains.common.network.play

import ru.megains.common.network.INetHandler
import ru.megains.common.network.play.client._

trait INetHandlerPlayServer extends INetHandler {
    def processClickWindow(packetIn: CPacketClickWindow): Unit

    def processPlayer(packetIn: CPacketPlayer): Unit

    def processPlayerDigging(packetIn: CPacketPlayerDigging): Unit

    def processHeldItemChange(packetIn: CPacketHeldItemChange): Unit

    def processPlayerBlockPlacement(packetIn: CPacketPlayerTryUseItem): Unit

    def processRightClickBlock(packetIn: CPacketPlayerTryUseItemOnBlock): Unit

}
