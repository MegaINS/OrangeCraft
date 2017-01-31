package ru.megains.common.network.play.server

import ru.megains.common.network.play.server.SPacketPlayerListItem.Action.Action
import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.server.entity.EntityPlayerMP

class SPacketPlayerListItem extends Packet[INetHandlerPlayClient] {

    var action: Action = _
    var id: Int = 0
    var name: String = ""

    def this(actionIn: Action, playersIn: EntityPlayerMP) {
        this()
        action = actionIn
        name = playersIn.name
        id = playersIn.interactionManager.gameType.id
    }


    override def readPacketData(buf: PacketBuffer): Unit = {
        id = buf.readInt()
        name = buf.readStringFromBuffer(256)
    }


    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(id)
        buf.writeStringToBuffer(name)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handlePlayerListItem(this)
    }
}

object SPacketPlayerListItem {

    object Action extends Enumeration {
        type Action = Value
        val ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER = Value
    }

}
