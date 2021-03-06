package ru.megains.common.network.play.server

import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketSpawnPosition extends Packet[INetHandlerPlayClient] {

    var blockPos: BlockPos = _

    def this(blockPosIn: BlockPos) {
        this()
        blockPos = blockPosIn
    }

    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {

    }
}
