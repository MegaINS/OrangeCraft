package ru.megains.common.network.play.client

import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketPlayerTryUseItemOnBlock() extends Packet[INetHandlerPlayServer] {
    var posMouseOver: BlockPos = _
    var posBlockSet: BlockPos = _
    var placedBlockDirection: BlockDirection = _
    var facingX: Float = .0f
    var facingY: Float = .0f
    var facingZ: Float = .0f

    def this(posMouseOverIn: BlockPos, posBlockSetIn: BlockPos, placedBlockDirectionIn: BlockDirection, facingXIn: Float, facingYIn: Float, facingZIn: Float) {
        this()
        posMouseOver = posMouseOverIn
        posBlockSet = posBlockSetIn
        placedBlockDirection = placedBlockDirectionIn
        facingX = facingXIn
        facingY = facingYIn
        facingZ = facingZIn
    }

    /**
      * Reads the raw packet data from the data stream.
      */

    def readPacketData(buf: PacketBuffer) {
        posMouseOver = buf.readBlockPos
        posBlockSet = buf.readBlockPos
        placedBlockDirection = BlockDirection.DIRECTIONAL_BY_ID(buf.readByte())
        facingX = buf.readUnsignedByte.toFloat / 16.0F
        facingY = buf.readUnsignedByte.toFloat / 16.0F
        facingZ = buf.readUnsignedByte.toFloat / 16.0F
    }

    /**
      * Writes the raw packet data to the data stream.
      */

    def writePacketData(buf: PacketBuffer) {
        buf.writeBlockPos(posMouseOver)
        buf.writeBlockPos(posBlockSet)
        buf.writeByte(placedBlockDirection.id)

        buf.writeByte((facingX * 16.0F).toInt)
        buf.writeByte((facingY * 16.0F).toInt)
        buf.writeByte((facingZ * 16.0F).toInt)
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerPlayServer) {
        handler.processRightClickBlock(this)
    }

}
