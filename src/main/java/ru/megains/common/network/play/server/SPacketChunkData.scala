package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}
import ru.megains.game.block.Block
import ru.megains.game.blockdata.MultiBlockPos
import ru.megains.game.multiblock.MultiBlock
import ru.megains.game.world.chunk.Chunk
import ru.megains.game.world.storage.ExtendedBlockStorage

import scala.collection.mutable.ArrayBuffer

class SPacketChunkData extends Packet[INetHandlerPlayClient] {


    var chunkX: Int = 0
    var chunkY: Int = 0
    var chunkZ: Int = 0
    var blockStorage: ExtendedBlockStorage = _

    def this(chunkIn: Chunk, p_i47124_2: Int) {
        this()
        blockStorage = chunkIn.blockStorage
        chunkX = chunkIn.position.chunkX
        chunkY = chunkIn.position.chunkY
        chunkZ = chunkIn.position.chunkZ
    }

    override def readPacketData(buf: PacketBuffer): Unit = {
        chunkX = buf.readInt()
        chunkY = buf.readInt()
        chunkZ = buf.readInt()
        blockStorage = readChunk(buf)
    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeInt(chunkX)
        buf.writeInt(chunkY)
        buf.writeInt(chunkZ)
        buf.writeBytes(writeChunk().toArray)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handleChunkData(this)
    }

    def readChunk(buf: PacketBuffer): ExtendedBlockStorage = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val data = blockStorage.data
        val multiBlockStorage = blockStorage.multiBlockStorage
        for (i <- 0 until 4096) {
            data.set(i, readIntOfTwoByte(buf))
        }
        val sizeMultiBlock = readIntOfTwoByte(buf)
        for (i <- 0 until sizeMultiBlock) {
            val posMultiBlock = readIntOfTwoByte(buf)
            val multiBlock = new MultiBlock()
            val sizeBlock = readIntOfTwoByte(buf)
            for (j <- 0 until sizeBlock) {
                val posBlock = readIntOfTwoByte(buf)
                val blockId = readIntOfTwoByte(buf)
                multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock), Block.getBlockById(blockId))
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def writeChunk(): ArrayBuffer[Byte] = {
        val chunkData = ArrayBuffer[Byte]()
        val blockData = blockStorage.data

        for (i <- 0 until 4096) {
            addTwoByteOfInt(blockData.get(i), chunkData)
        }
        val multiBlockStorage = blockStorage.multiBlockStorage

        //Колличество мультиБлоков
        addTwoByteOfInt(multiBlockStorage.size, chunkData)

        multiBlockStorage.foreach(
            (data: (Int, MultiBlock)) => {
                //Координаты мультиБлока
                addTwoByteOfInt(data._1, chunkData)
                val blockData = data._2.blockData
                //Колличество блоков в мультиБлоке
                addTwoByteOfInt(blockData.size, chunkData)
                blockData.foreach(
                    (data2: (MultiBlockPos, Block)) => {
                        //Координаты блока
                        addTwoByteOfInt(data2._1.getIndex, chunkData)
                        //Id блока
                        addTwoByteOfInt(Block.getIdByBlock(data2._2), chunkData)
                    }
                )
            }
        )
        chunkData
    }

    def addTwoByteOfInt(int: Int, buffer: ArrayBuffer[Byte]): Unit = {
        buffer += (int >> 8).toByte
        buffer += int.toByte
    }

    def readIntOfTwoByte(buf: PacketBuffer): Int = {
        buf.readByte() << 8 | buf.readByte()
    }
}
