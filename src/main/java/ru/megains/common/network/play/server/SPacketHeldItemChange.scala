package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketHeldItemChange extends Packet[INetHandlerPlayClient] {

    var heldItemHotbarIndex: Int = -1

    def this(hotbarIndexIn: Int) {
        this()
        heldItemHotbarIndex = hotbarIndexIn
    }


    override def readPacketData(packetBuffer: PacketBuffer): Unit = {
        heldItemHotbarIndex = packetBuffer.readByte
    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {
        packetBuffer.writeByte(heldItemHotbarIndex)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handleHeldItemChange(this)
    }
}
