package ru.megains.common.network.login.server

import java.io.IOException

import ru.megains.common.network.login.INetHandlerLoginClient
import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketDisconnect() extends Packet[INetHandlerLoginClient] {

    private var reason: String = _

    def this(text: String) {
        this()
        this.reason = text
    }

    /**
      * Reads the raw packet data from the data stream.
      */
    @throws[IOException]
    def readPacketData(buf: PacketBuffer) {
        this.reason = buf.readStringFromBuffer(32767)
    }

    /**
      * Writes the raw packet data to the data stream.
      */
    @throws[IOException]
    def writePacketData(buf: PacketBuffer) {
        buf.writeStringToBuffer(reason)
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerLoginClient) {
        handler.handleDisconnect(this)
    }


}
