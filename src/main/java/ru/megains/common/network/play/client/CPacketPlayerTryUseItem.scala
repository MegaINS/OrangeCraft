package ru.megains.common.network.play.client

import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketPlayerTryUseItem() extends Packet[INetHandlerPlayServer] {

    /**
      * Reads the raw packet data from the data stream.
      */

    def readPacketData(buf: PacketBuffer) {

    }

    /**
      * Writes the raw packet data to the data stream.
      */

    def writePacketData(buf: PacketBuffer) {

    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerPlayServer) {
        handler.processPlayerBlockPlacement(this)
    }

}
