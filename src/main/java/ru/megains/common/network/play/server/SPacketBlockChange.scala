package ru.megains.common.network.play.server

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.common.register.Blocks
import ru.megains.common.world.World

class SPacketBlockChange() extends Packet[INetHandlerPlayClient] {

    var blockPosition: BlockPos = _
    var block: Block = _
    var meta: Int = _

    def this(worldIn: World, posIn: BlockPos) {
        this()
        blockPosition = posIn
        block = worldIn.getBlock(posIn)
        meta = worldIn.getBlockMeta(posIn)
    }

    /**
      * Reads the raw packet data from the data stream.
      */

    def readPacketData(buf: PacketBuffer) {
        blockPosition = buf.readBlockPos
        block = Blocks.getBlockById(buf.readVarIntFromBuffer)
        meta = buf.readInt()
    }

    /**
      * Writes the raw packet data to the data stream.
      */

    def writePacketData(buf: PacketBuffer) {
        buf.writeBlockPos(blockPosition)
        buf.writeVarIntToBuffer(Blocks.getIdByBlock(block))
        buf.writeInt(meta)
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerPlayClient) {
        handler.handleBlockChange(this)
    }


}
