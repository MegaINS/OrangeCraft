package ru.megains.common.network.play.server

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.MultiBlockPos
import ru.megains.common.multiblock.MultiBlock
import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.common.world.chunk.Chunk
import ru.megains.common.world.storage.ExtendedBlockStorage

class SPacketChunkData extends Packet[INetHandlerPlayClient] {


    var chunkX: Int = 0
    var chunkY: Int = 0
    var chunkZ: Int = 0
    var blockStorage: ExtendedBlockStorage = _
    var chunkVoid: Boolean = false

    def this(chunkIn: Chunk) {
        this()
        blockStorage = chunkIn.blockStorage
        chunkVoid = chunkIn.isVoid
        chunkX = chunkIn.position.chunkX
        chunkY = chunkIn.position.chunkY
        chunkZ = chunkIn.position.chunkZ
    }

    override def readPacketData(buf: PacketBuffer): Unit = {
        chunkX = buf.readInt()
        chunkY = buf.readInt()
        chunkZ = buf.readInt()
        chunkVoid = buf.readBoolean()
        blockStorage = if (chunkVoid) new ExtendedBlockStorage else readChunk(buf)
    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(chunkX)
        buf.writeInt(chunkY)
        buf.writeInt(chunkZ)
        buf.writeBoolean(chunkVoid)
        if (!chunkVoid) writeChunk(buf)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handleChunkData(this)
    }

    def readChunk(buf: PacketBuffer): ExtendedBlockStorage = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val blocksId = blockStorage.blocksId
        val blocksHp = blockStorage.blocksHp
        val multiBlockStorage = blockStorage.multiBlockStorage
        for (i <- 0 until 4096) {
            blocksId(i) = buf.readShort()
            blocksHp(i) = buf.readInt()
        }

        val sizeMultiBlock = buf.readInt()
        for (_ <- 0 until sizeMultiBlock) {

            val posMultiBlock = buf.readInt()
            val multiBlock = new MultiBlock()
            val blockData = multiBlock.blockData

            val sizeBlock = buf.readInt()
            for (_ <- 0 until sizeBlock) {

                val posBlock = buf.readInt()
                val blockId = buf.readInt()
                val blockHp = buf.readInt()
                blockData += MultiBlockPos.getForIndex(posBlock) -> (Block.getBlockById(blockId), blockHp)
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def writeChunk(buf: PacketBuffer): Unit = {

        val blocksId = blockStorage.blocksId
        val blockHp = blockStorage.blocksHp
        for (i <- 0 until 4096) {
            buf.writeShort(blocksId(i))
            buf.writeInt(blockHp(i))
        }


        val multiBlockStorage = blockStorage.multiBlockStorage

        //Колличество мультиБлоков
        buf.writeInt(multiBlockStorage.size)


        multiBlockStorage.foreach(
            (data: (Int, MultiBlock)) => {
                //Координаты мультиБлока
                buf.writeInt(data._1)

                val blockData = data._2.blockData
                //Колличество блоков в мультиБлоке
                buf.writeInt(blockData.size)

                blockData.foreach(
                    (data2: (MultiBlockPos, (Block, Int))) => {
                        //Координаты блока
                        buf.writeInt(data2._1.getIndex)
                        //Id блока
                        buf.writeInt(Block.getIdByBlock(data2._2._1))
                        //Hp блока
                        buf.writeInt(data2._2._2)
                    }
                )
            }
        )

    }

}
