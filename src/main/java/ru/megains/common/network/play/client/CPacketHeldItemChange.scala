package ru.megains.common.network.play.client

import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketHeldItemChange() extends Packet[INetHandlerPlayServer] {
    var slotId: Int = 0

    def this(slotIdIn: Int) {
        this()
        slotId = slotIdIn
    }


    def readPacketData(buf: PacketBuffer) {
        slotId = buf.readShort
    }


    def writePacketData(buf: PacketBuffer) {
        buf.writeShort(slotId)
    }

    def processPacket(handler: INetHandlerPlayServer) {
        handler.processHeldItemChange(this)
    }
}
