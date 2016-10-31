package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketUnloadChunk extends Packet[INetHandlerPlayClient] {

    var x = 0
    var y = 0
    var z = 0

    def this(xIn: Int, yIn: Int, zIn: Int) {
        this()
        x = xIn
        y = yIn
        z = zIn
    }

    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {

    }
}
