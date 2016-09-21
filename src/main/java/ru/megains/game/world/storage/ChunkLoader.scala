package ru.megains.game.world.storage

import java.io._
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import ru.megains.game.block.Block
import ru.megains.game.blockdata.MultiBlockPos
import ru.megains.game.multiblock.MultiBlock
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.Blocks
import ru.megains.game.world.World
import ru.megains.game.world.chunk.{Chunk, ChunkVoid, Loader}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.io.Directory

class ChunkLoader(chunkSaveDirectory: Directory) {

    chunkSaveDirectory.createDirectory()


    def loadChunk(world: World, x: Int, y: Int, z: Int) = {
        new Thread(new Loader(world, this, x, y, z)).start()
    }


    def load(world: World, x: Int, y: Int, z: Int): Chunk = {
        val input = load(Chunk.getIndex(x, y, z) toString)

        if (input == null) {
            loadDefaultChunk(world, x, y, z)
        } else {
            try {
                val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
                val data = blockStorage.data
                val multiBlockStorage = blockStorage.multiBlockStorage
                for (i <- 0 until 4096) {
                    data.set(i, readIntOfTwoByte(input))
                }
                val sizeMultiBlock = readIntOfTwoByte(input)
                for (i <- 0 until sizeMultiBlock) {
                    val posMultiBlock = readIntOfTwoByte(input)
                    val multiBlock = new MultiBlock()
                    val sizeBlock = readIntOfTwoByte(input)
                    for (j <- 0 until sizeBlock) {
                        val posBlock = readIntOfTwoByte(input)
                        val blockId = readIntOfTwoByte(input)
                        multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock), Block.getBlockById(blockId))
                    }
                    multiBlockStorage.put(posMultiBlock, multiBlock)
                }
                new Chunk(world, new ChunkPosition(x, y, z), blockStorage)
            }
            catch {
                case var3: Exception =>
                    loadDefaultChunk(world, x, y, z)
            }

        }

    }


    def loadDefaultChunk(world: World, x: Int, y: Int, z: Int): Chunk = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val blockData = blockStorage.data

        if (y < 0) {
            for (i <- 0 until 4096) {
                blockData.set(i, Block.getIdByBlock(Blocks.stone))
            }
        } else {
            val array = world.heightMap.generateHeightMap(x, z)
            for (x1 <- 0 to 15; y1 <- 0 to 15; z1 <- 0 to 15) {
                if (array(x1)(z1) > y1 + (y * Chunk.CHUNK_SIZE)) {
                    blockStorage.setBlockId(x1, y1, z1, Block.getIdByBlock(Blocks.stone))
                } else if (array(x1)(z1) == y1 + (y * Chunk.CHUNK_SIZE)) {
                    blockStorage.setBlockId(x1, y1, z1, Block.getIdByBlock(Blocks.grass))
                } else {
                    blockStorage.setBlockId(x1, y1, z1, Block.getIdByBlock(Blocks.air))
                }
            }
        }


        new Chunk(world, new ChunkPosition(x, y, z), blockStorage)
    }


    def load(fileName: String): DataInputStream = {

        try {
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(chunkSaveDirectory.jfile, fileName + ".dat"))))
            e
        }
        catch {
            case var3: Exception =>
                println("Error load chunk " + fileName + ".dat")
                null
        }
    }

    def readIntOfTwoByte(input: DataInputStream): Int = {
        input.readByte() << 8 | input.readByte()
    }


    def save(chunk: Chunk): Unit = {

        if (!chunk.isInstanceOf[ChunkVoid]) {
            val chunkData = ArrayBuffer[Byte]()
            val blockData = chunk.blockStorage.data

            for (i <- 0 until 4096) {
                addTwoByteOfInt(blockData.get(i), chunkData)
            }
            val multiBlockStorage = chunk.blockStorage.multiBlockStorage

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
            save(Chunk.getIndex(chunk) toString, chunkData toArray)
        }
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

    def addTwoByteOfInt(int: Int, buffer: ArrayBuffer[Byte]): Unit = {
        buffer += (int >> 8).toByte
        buffer += int.toByte
    }
}
