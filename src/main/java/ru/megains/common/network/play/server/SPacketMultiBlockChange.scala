package ru.megains.common.network.play.server

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.common.register.Blocks
import ru.megains.common.world.chunk.Chunk

import scala.collection.mutable.ArrayBuffer


class SPacketMultiBlockChange() extends Packet[INetHandlerPlayClient] {


    var changedBlocks: Array[BlockUpdateData] = _

    def this(changedBlocksIn: ArrayBuffer[BlockPos], chunk: Chunk) {
        this()
        changedBlocks = new Array[BlockUpdateData](changedBlocksIn.length)
        for (i <- changedBlocksIn.indices) {
            changedBlocks(i) = new BlockUpdateData(changedBlocksIn(i), chunk)
        }

    }


    def readPacketData(buf: PacketBuffer) {

        changedBlocks = new Array[BlockUpdateData](buf.readVarIntFromBuffer)
        for (i <- changedBlocks.indices) {
            changedBlocks(i) = new BlockUpdateData(buf.readBlockPos(), Blocks.getBlockById(buf.readVarIntFromBuffer))
        }
    }


    def writePacketData(buf: PacketBuffer) {

        buf.writeVarIntToBuffer(changedBlocks.length)
        for (blockData <- changedBlocks) {
            buf.writeBlockPos(blockData.blockPosition)
            buf.writeVarIntToBuffer(Blocks.getIdByBlock(blockData.block))
        }
    }


    def processPacket(handler: INetHandlerPlayClient) {
        handler.handleMultiBlockChange(this)
    }


    class BlockUpdateData {

        var blockPosition: BlockPos = _
        var block: Block = _

        def this(blockPositionIn: BlockPos, blockIn: Block) {
            this()
            blockPosition = blockPositionIn
            block = blockIn
        }

        def this(blockPositionIn: BlockPos, chunk: Chunk) {
            this()
            blockPosition = blockPositionIn
            block = chunk.getBlockWorldCord(blockPosition)
        }

    }

}
