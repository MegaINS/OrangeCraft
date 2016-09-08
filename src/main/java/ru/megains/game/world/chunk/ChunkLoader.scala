package ru.megains.game.world.chunk

import java.io.{File => JFile, _}
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import ru.megains.game.block.Block
import ru.megains.game.blockdata.MultiBlockPos
import ru.megains.game.multiblock.MultiBlock
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.Blocks
import ru.megains.game.world.World
import ru.megains.game.world.chunk.storage.ExtendedBlockStorage

import scala.collection.mutable.ArrayBuffer
import scala.reflect.io.File


object ChunkLoader {


    var sizeWorld: Int = 0

    private val LOCATION: String = getClass.getClassLoader.getResource("").getPath
    private val WORLD_LOCATION: String = LOCATION + "world/chunk/"
    File(WORLD_LOCATION).createDirectory()


//    def load(world: World, x: Int, y: Int, z: Int): Chunk = {
//
//        val chunk: Array[Byte] = new Array[Byte](4096)
//
//        if (!load(Chunk.getIndex(x, y, z) toString, chunk)) {
//
//            for (i <- chunk.indices) {
//                if (y < 0) {
//                    chunk(i) = Block.getIdByBlock(Blocks.stone) toByte
//                }
//                else {
//                    chunk(i) = Block.getIdByBlock(Blocks.air) toByte
//                }
//            }
//        }
//        new Chunk(world, new ChunkPosition(x, y, z), chunk)
//    }

    def load(world: World, x: Int, y: Int, z: Int): Chunk ={
        val input = load(Chunk.getIndex(x, y, z) toString)

        if (input == null) {
            loadDefaultChunk(world, x, y, z)
        }else{
            try {
                val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
                val data = blockStorage.data
                val multiBlockStorage = blockStorage.multiBlockStorage
                for (i <- 0 until 4096) {
                    data.set(i,readIntOfTwoByte(input))
                }
                val sizeMultiBlock = readIntOfTwoByte(input)
                for(i <- 0 until sizeMultiBlock) {
                    val posMultiBlock = readIntOfTwoByte(input)
                    val multiBlock = new MultiBlock()
                    val sizeBlock = readIntOfTwoByte(input)
                    for(j<- 0 until sizeBlock){
                        val posBlock = readIntOfTwoByte(input)
                        val blockId = readIntOfTwoByte(input)
                        multiBlock.putBlock(MultiBlockPos.getForIndex(posBlock),Block.getBlockById(blockId))
                    }
                    multiBlockStorage.put(posMultiBlock,multiBlock)
                }
                new Chunk(world, new ChunkPosition(x, y, z), blockStorage)
            }
            catch {
                case var3: Exception =>
                    println("Error load chunk " + Chunk.getIndex(x, y, z) + ".dat")
                    loadDefaultChunk(world, x, y, z)
            }

        }

    }


    def loadDefaultChunk(world: World, x: Int, y: Int, z: Int):Chunk = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val blockData = blockStorage.data
        for (i <- 0 until 4096) {
            if (y < 0) {
                blockData.set(i,Block.getIdByBlock(Blocks.stone))
            }
            else {
                blockData.set(i,Block.getIdByBlock(Blocks.air))
            }
        }
        new Chunk(world, new ChunkPosition(x, y, z), blockStorage)
    }
    def load(fileName: String): DataInputStream = {

        try {
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new JFile(WORLD_LOCATION + fileName + ".dat"))))
            e
        }
        catch {
            case var3: Exception =>
                println("Error load chunk " + fileName + ".dat")
                null
        }


    }

//    def load(fileName: String, data: Array[Byte]): Boolean = {
//
//        try {
//            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new JFile(WORLD_LOCATION + fileName + ".dat"))))
//            e.readFully(data)
//            e.close()
//            true
//        }
//        catch {
//            case var3: Exception =>
//                println("Error load chunk " + fileName + ".dat")
//                false
//        }
//
//
//    }

    def save(chunk: Chunk): Unit ={

        val chunkData =  ArrayBuffer[Byte]()
        val blockData = chunk.blockStorage.data

        for(i<- 0 until 4096){
            addTwoByteOfInt(blockData.get(i),chunkData)
        }
        val multiBlockStorage = chunk.blockStorage.multiBlockStorage

        //Колличество мультиБлоков
        addTwoByteOfInt(multiBlockStorage.size,chunkData)

        multiBlockStorage.foreach(
            (data: (Int, MultiBlock))=> {
                //Координаты мультиБлока
                addTwoByteOfInt(data._1,chunkData)
                val blockData = data._2.blockData
                //Колличество блоков в мультиБлоке
                addTwoByteOfInt(blockData.size,chunkData)
                blockData.foreach(
                    (data2:(MultiBlockPos, Block))=>{
                        //Координаты блока
                        addTwoByteOfInt(data2._1.getIndex, chunkData)
                        //Id блока
                        addTwoByteOfInt(Block.getIdByBlock(data2._2),chunkData)
                    }
                )
            }
        )
        save(Chunk.getIndex(chunk) toString,chunkData toArray)
    }

    def addTwoByteOfInt(int:Int,buffer:ArrayBuffer[Byte]): Unit ={
        buffer += (int >> 8).toByte
        buffer += int.toByte
    }
    def readIntOfTwoByte(input:DataInputStream): Int ={
        input.readByte() << 8 | input.readByte()
    }


    def save(fileName: String, data: Array[Byte]) {


        try {
            val e: DataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new JFile(WORLD_LOCATION + fileName + ".dat"))))
            e.write(data)
            e.close()
        }
        catch {
            case var2: Exception =>
                println("Error save chunk " + fileName + ".dat")
        }
    }

}
