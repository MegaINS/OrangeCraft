package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.game.block.Block
import ru.megains.game.blockdata.BlockPos
import ru.megains.game.world.World

class SPacketBlockChange() extends Packet[INetHandlerPlayClient] {

    var blockPosition: BlockPos = _
    var block: Block = _

    def this(worldIn: World, posIn: BlockPos) {
        this()
        blockPosition = posIn
        block = worldIn.getBlock(posIn)
    }

    /**
      * Reads the raw packet data from the data stream.
      */

    def readPacketData(buf: PacketBuffer) {
        blockPosition = buf.readBlockPos
        block = Block.getBlockById(buf.readVarIntFromBuffer)
    }

    /**
      * Writes the raw packet data to the data stream.
      */

    def writePacketData(buf: PacketBuffer) {
        buf.writeBlockPos(blockPosition)
        buf.writeVarIntToBuffer(Block.getIdByBlock(block))
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerPlayClient) {
        handler.handleBlockChange(this)
    }


}
