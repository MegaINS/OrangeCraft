package ru.megains.common.network.play.client

import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketPlayerTryUseItem() extends Packet[INetHandlerPlayServer] {


    def readPacketData(buf: PacketBuffer) {

    }


    def writePacketData(buf: PacketBuffer) {

    }

    def processPacket(handler: INetHandlerPlayServer) {
        handler.processPlayerBlockPlacement(this)
    }

}
