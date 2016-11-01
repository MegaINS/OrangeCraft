package ru.megains.game.world.storage

import java.io._
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import io.netty.buffer.Unpooled
import ru.megains.common.network.PacketBuffer
import ru.megains.game.block.Block
import ru.megains.game.blockdata.MultiBlockPos
import ru.megains.game.multiblock.MultiBlock
import ru.megains.game.position.ChunkPosition
import ru.megains.game.world.World
import ru.megains.game.world.chunk.{Chunk, ChunkVoid, Loader}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.io.Directory

class ChunkLoader(chunkSaveDirectory: Directory) {

    chunkSaveDirectory.createDirectory()


    def loadChunk(world: World, x: Int, y: Int, z: Int) = {
        new Thread(new Loader(world, this, x, y, z)).start()
    }


    //    def load(world: World, x: Int, y: Int, z: Int): Chunk = {
    //        val input = load(Chunk.getIndex(x, y, z) toString)
    //
    //        if (input != null) {
    //            try {
    //                val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
    //                val data = blockStorage.data
    //                val multiBlockStorage = blockStorage.multiBlockStorage
    //                for (i <- 0 until 4096) {
    //                    data.set(i, readIntOfTwoByte(input))
    //                }
    //                val sizeMultiBlock = readIntOfTwoByte(input)
    //                for (i <- 0 until sizeMultiBlock) {
    //                    val posMultiBlock = readIntOfTwoByte(input)
    //                    val multiBlock = new MultiBlock()
    //                    val sizeBlock = readIntOfTwoByte(input)
    //                    for (j <- 0 until sizeBlock) {
    //                        val posBlock = readIntOfTwoByte(input)
    //                        val blockId = readIntOfTwoByte(input)
    //                        multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock), Block.getBlockById(blockId))
    //                    }
    //                    multiBlockStorage.put(posMultiBlock, multiBlock)
    //                }
    //                return new Chunk(world, new ChunkPosition(x, y, z), blockStorage)
    //            }
    //            catch {
    //                case var3: Exception =>
    //                    println("Error load chunk")
    //            }
    //        }
    //        null
    //    }

    def load(world: World, x: Int, y: Int, z: Int): Chunk = {
        val input = load(Chunk.getIndex(x, y, z) toString)

        if (input != null) {
            try {
                return new Chunk(world, new ChunkPosition(x, y, z), readChunk(input))
            }
            catch {
                case var3: Exception =>
                    println("Error load chunk")
            }
        }
        null
    }

    def readChunk(buf: DataInputStream): ExtendedBlockStorage = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val data = blockStorage.data
        val multiBlockStorage = blockStorage.multiBlockStorage
        for (i <- 0 until 4096) {
            data.set(i, buf.readInt())
        }
        val sizeMultiBlock = buf.readInt()
        for (i <- 0 until sizeMultiBlock) {
            val posMultiBlock = buf.readInt()
            val multiBlock = new MultiBlock()
            val sizeBlock = buf.readInt()
            for (j <- 0 until sizeBlock) {
                val posBlock = buf.readInt()
                val blockId = buf.readInt()
                multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock), Block.getBlockById(blockId))
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def load(fileName: String): DataInputStream = {

        try {
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(chunkSaveDirectory.jfile, fileName + ".dat"))))
            e
        }
        catch {
            case var3: Exception =>
                null
        }
    }

    def readIntOfTwoByte(input: DataInputStream): Int = {
        input.readByte() << 8 | input.readByte()
    }


    //    def save(chunk: Chunk): Unit = {
    //
    //        if (!chunk.isInstanceOf[ChunkVoid] && chunk.isSaved) {
    //            val chunkData = ArrayBuffer[Byte]()
    //            val blockData = chunk.blockStorage.data
    //
    //            for (i <- 0 until 4096) {
    //                addTwoByteOfInt(blockData.get(i), chunkData)
    //            }
    //            val multiBlockStorage = chunk.blockStorage.multiBlockStorage
    //
    //            //Колличество мультиБлоков
    //            addTwoByteOfInt(multiBlockStorage.size, chunkData)
    //
    //            multiBlockStorage.foreach(
    //                (data: (Int, MultiBlock)) => {
    //                    //Координаты мультиБлока
    //                    addTwoByteOfInt(data._1, chunkData)
    //                    val blockData = data._2.blockData
    //                    //Колличество блоков в мультиБлоке
    //                    addTwoByteOfInt(blockData.size, chunkData)
    //                    blockData.foreach(
    //                        (data2: (MultiBlockPos, Block)) => {
    //                            //Координаты блока
    //                            addTwoByteOfInt(data2._1.getIndex, chunkData)
    //                            //Id блока
    //                            addTwoByteOfInt(Block.getIdByBlock(data2._2), chunkData)
    //                        }
    //                    )
    //                }
    //            )
    //            save(Chunk.getIndex(chunk) toString, chunkData toArray)
    //        }
    //    }

    def save(chunk: Chunk): Unit = {

        if (!chunk.isInstanceOf[ChunkVoid] && chunk.isSaved) {
            val chunkData = new PacketBuffer(Unpooled.buffer())


            writeChunk(chunkData, chunk.blockStorage)
            save(Chunk.getIndex(chunk) toString, chunkData.array())
        }
    }

    def writeChunk(buf: PacketBuffer, blockStorage: ExtendedBlockStorage): Unit = {

        val blockData = blockStorage.data

        for (i <- 0 until 4096) {
            buf.writeInt(blockData.get(i))
        }

        val multiBlockStorage = blockStorage.multiBlockStorage
        buf.writeInt(multiBlockStorage.size)

        multiBlockStorage.foreach(
            (data: (Int, MultiBlock)) => {
                //Координаты мультиБлока
                buf.writeInt(data._1)
                val blockData = data._2.blockData
                //Колличество блоков в мультиБлоке
                buf.writeInt(blockData.size)
                blockData.foreach(
                    (data2: (MultiBlockPos, Block)) => {
                        //Координаты блока
                        buf.writeInt(data2._1.getIndex)
                        //Id блока
                        buf.writeInt(Block.getIdByBlock(data2._2))

                    }
                )
            }
        )
    }


    def save(fileName: String, data: Array[Byte]) {


        try {
            val e: DataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File(chunkSaveDirectory.jfile, fileName + ".dat"))))
            e.write(data)
            e.close()
        }
        catch {
            case var2: Exception =>
                println("Error save chunk " + fileName + ".dat")
        }
    }


    def saveExtraChunkData(worldIn: World, chunkIn: Chunk) {
    }

    def addTwoByteOfInt(int: Int, buffer: ArrayBuffer[Byte]): Unit = {
        buffer += (int >> 8).toByte
        buffer += int.toByte
    }
}
