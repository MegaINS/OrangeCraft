package ru.megains.common.network.status.client

import java.io.IOException

import ru.megains.common.network.status.INetHandlerStatusServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketServerQuery extends Packet[INetHandlerStatusServer] {
    /**
      * Reads the raw packet data from the data stream.
      */
    @throws[IOException]
    def readPacketData(buf: PacketBuffer) {
    }

    /**
      * Writes the raw packet data to the data stream.
      */
    @throws[IOException]
    def writePacketData(buf: PacketBuffer) {
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerStatusServer) {
        handler.processServerQuery(this)
    }
}
