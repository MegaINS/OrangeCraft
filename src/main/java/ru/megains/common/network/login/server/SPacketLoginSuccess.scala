package ru.megains.common.network.login.server


import ru.megains.common.network.login.INetHandlerLoginClient
import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketLoginSuccess extends Packet[INetHandlerLoginClient] {


    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def processPacket(handler: INetHandlerLoginClient): Unit = {
        handler.handleLoginSuccess(this)
    }
}
