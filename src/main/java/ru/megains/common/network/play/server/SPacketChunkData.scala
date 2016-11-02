package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.game.block.Block
import ru.megains.game.blockdata.MultiBlockPos
import ru.megains.game.multiblock.MultiBlock
import ru.megains.game.world.chunk.Chunk
import ru.megains.game.world.storage.ExtendedBlockStorage

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
        val data = blockStorage.data
        val multiBlockStorage = blockStorage.multiBlockStorage
        for (i <- 0 until 4096) {
            // data.set(i, readIntOfTwoByte(buf))
            data.set(i, buf.readInt())
        }
        //  val sizeMultiBlock = readIntOfTwoByte(buf)
        val sizeMultiBlock = buf.readInt()
        for (i <- 0 until sizeMultiBlock) {
            //  val posMultiBlock = readIntOfTwoByte(buf)
            val posMultiBlock = buf.readInt()
            val multiBlock = new MultiBlock()
            // val sizeBlock = readIntOfTwoByte(buf)
            val sizeBlock = buf.readInt()
            for (j <- 0 until sizeBlock) {
                //  val posBlock = readIntOfTwoByte(buf)
                //  val blockId = readIntOfTwoByte(buf)
                val posBlock = buf.readInt()
                val blockId = buf.readInt()
                multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock), Block.getBlockById(blockId))
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def writeChunk(buf: PacketBuffer): Unit = {
        //  val chunkData = ArrayBuffer[Byte]()
        val blockData = blockStorage.data

        for (i <- 0 until 4096) {
            buf.writeInt(blockData.get(i))
            //  addTwoByteOfInt(blockData.get(i), chunkData)
        }

        //  buf.writeBytes(chunkData.toArray)

        val multiBlockStorage = blockStorage.multiBlockStorage

        //Колличество мультиБлоков
        buf.writeInt(multiBlockStorage.size)
        //  addTwoByteOfInt(multiBlockStorage.size, chunkData)

        multiBlockStorage.foreach(
            (data: (Int, MultiBlock)) => {
                //Координаты мультиБлока
                buf.writeInt(data._1)
                // addTwoByteOfInt(data._1, chunkData)
                val blockData = data._2.blockData
                //Колличество блоков в мультиБлоке
                buf.writeInt(blockData.size)
                //  addTwoByteOfInt(blockData.size, chunkData)
                blockData.foreach(
                    (data2: (MultiBlockPos, Block)) => {
                        //Координаты блока
                        buf.writeInt(data2._1.getIndex)
                        //  addTwoByteOfInt(data2._1.getIndex, chunkData)
                        //Id блока
                        buf.writeInt(Block.getIdByBlock(data2._2))
                        // addTwoByteOfInt(Block.getIdByBlock(data2._2), chunkData)
                    }
                )
            }
        )
        // chunkData
    }

    //    def addTwoByteOfInt(int: Int, buffer: ArrayBuffer[Byte]): Unit = {
    //        buffer += (int >> 8).toByte
    //        buffer += int.toByte
    //    }

    //    def readIntOfTwoByte(buf: PacketBuffer): Int = {
    //        buf.readByte() << 8 | buf.readByte()
    //    }
}
