package ru.megains.game.world.chunk

import java.io._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}

import ru.megains.engine.graph.RenderChunk
import ru.megains.game.block.Block
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.Blocks
import ru.megains.game.world.World

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object ChunkLoader {



    var sizeWorld:Int =0
    private val WORLD_DIRECTORY: String = "src/world/world.dat"
    private val WORLD_LOCATION: String = "src/world/"



    def load(world: Array[Byte]): Boolean = {
        try {
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(WORLD_DIRECTORY))))
            e.readFully(world)
            e.close()
        }
        catch {
            case var3: Exception =>
                var3.printStackTrace()
                return false
        }
         true
    }





    def load(chunks: mutable.HashMap[Long, Chunk], size: Int, world: World): Boolean = {
        sizeWorld = size * 8
        val loadW: Array[Byte] = new Array[Byte](sizeWorld)
        if (load(loadW)) {
            val worldBuf: ArrayBuffer[Byte] = new ArrayBuffer[Byte](4096)
            var i: Int = 0

            for(x <- -2 to 1;y <- -2 to 1;z <- -2 to 1){
                worldBuf.clear()
                worldBuf.copyToArray(loadW, i, 4096)
                i += 4096
                chunks.put(Chunk.getIndex(x, y, z), new Chunk(world, new ChunkPosition(x * RenderChunk.chunkSize, y * RenderChunk.chunkSize, z * RenderChunk.chunkSize), worldBuf.toArray))

            }


        }
        else {

            val wor: Array[Byte] = new Array[Byte](4096)

            for(x <- -4 to 3;y <- -4 to 3;z <- -4 to 3){

                for(i<- wor.indices){
                    if (y < 0) {
                       wor(i) = 2
                    }
                    else {
                        wor(i) = 1
                    }
                }
                chunks.put(Chunk.getIndex(x, y, z), new Chunk(world, new ChunkPosition(x * RenderChunk.chunkSize, y * RenderChunk.chunkSize, z * RenderChunk.chunkSize), wor))
            }

        }
        true
    }

    def load(world:World,x:Int,y:Int,z:Int):Chunk ={

        val chunk: Array[Byte] = new Array[Byte](4096)

        if( !load(Chunk.getIndex(x,y,z) toString,chunk)){

            for(i<- chunk.indices){
                if (y < 0) {
                    chunk(i) = Block.getIdByBlock(Blocks.stone) toByte
                }
                else {
                    chunk(i) =  Block.getIdByBlock(Blocks.air) toByte
                }
            }
        }
        new Chunk(world,new ChunkPosition(x,y,z),chunk)
    }



    def load(fileName:String,data: Array[Byte]): Boolean = {

        try {
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(WORLD_LOCATION + fileName+".dat"))))
            e.readFully(data)
            e.close()
            true
        }
        catch {
            case var3: Exception =>
                println("Error load chunk "+ WORLD_LOCATION + fileName+".dat")
                false
        }


    }

    def save(fileName:String,data: Array[Byte]) {
        try {
            val e: DataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File(WORLD_LOCATION + fileName+".dat"))))
            e.write(data)
            e.close()
        }
        catch {
            case var2: Exception =>
                println("Error save chunk "+ WORLD_LOCATION + fileName+".dat")
        }
    }

}
