package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketJoinGame extends Packet[INetHandlerPlayClient] {


    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handleJoinGame(this)
    }
}
