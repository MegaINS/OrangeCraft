package ru.megains.game.world.chunk

import java.io.{File => JFile, _}
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import ru.megains.game.block.Block
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.Blocks
import ru.megains.game.world.World

import scala.reflect.io.File


object ChunkLoader {



    var sizeWorld:Int =0

    private val LOCATION:String = getClass.getClassLoader.getResource("").getPath
    private val WORLD_LOCATION: String =LOCATION+ "world/chunk/"
    File(WORLD_LOCATION).createDirectory()


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
            val e: DataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(new JFile(WORLD_LOCATION + fileName+".dat"))))
            e.readFully(data)
            e.close()
            true
        }
        catch {
            case var3: Exception =>
                println("Error load chunk " + fileName+".dat")
                false
        }


    }

    def save(fileName:String,data: Array[Byte]) {


        try {
            val e: DataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new JFile(WORLD_LOCATION + fileName+".dat"))))
            e.write(data)
            e.close()
        }
        catch {
            case var2: Exception =>
                println("Error save chunk " + fileName+".dat")
        }
    }

}
