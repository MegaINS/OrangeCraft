package ru.megains.common.world.gen

import ru.megains.common.block.Block
import ru.megains.common.position.ChunkPosition
import ru.megains.common.register.Blocks
import ru.megains.common.world.World
import ru.megains.common.world.chunk.{Chunk, IChunkGenerator}
import ru.megains.common.world.storage.ExtendedBlockStorage

import scala.util.Random

class ChunkProviderOverworld(world: World, seed: Long, mapFeaturesEnabledIn: Boolean, p_i46668_5: String) extends IChunkGenerator {

    val rand: Random = new Random()


    override def provideChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk = {
        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val blockData = blockStorage.blocksId

        if (chunkY < 0) {
            for (i <- 0 until 4096) {
                blockData(i) = Block.getIdByBlock(Blocks.stone).toShort
            }
        } else {
            val array = world.heightMap.generateHeightMap(chunkX, chunkZ)
            for (x1 <- 0 to 15; y1 <- 0 to 15; z1 <- 0 to 15) {
                if (array(x1)(z1) > y1 + (chunkY * Chunk.CHUNK_SIZE)) {
                    blockStorage.setBlock(x1, y1, z1, Blocks.stone)
                } else if (array(x1)(z1) == y1 + (chunkY * Chunk.CHUNK_SIZE)) {
                    blockStorage.setBlock(x1, y1, z1, Blocks.grass)
                } else {
                    blockStorage.setBlock(x1, y1, z1, Blocks.air)
                }
            }
        }


        new Chunk(world, new ChunkPosition(chunkX, chunkY, chunkZ), blockStorage)
    }

    //    def provideChunk(x: Int, z: Int): Chunk = {
    ////        this.rand.setSeed(x.toLong * 341873128712L + z.toLong * 132897987541L)
    ////        val chunkprimer: ChunkPrimer = new ChunkPrimer
    ////        this.setBlocksInChunk(x, z, chunkprimer)
    ////        this.biomesForGeneration = this.worldObj.getBiomeProvider.loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16)
    ////        this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration)
    ////       // if (this.settings.useCaves) this.caveGenerator.generate(this.worldObj, x, z, chunkprimer)
    ////       // if (this.settings.useRavines) this.ravineGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////        if (this.mapFeaturesEnabled) {
    //////            if (this.settings.useMineShafts) this.mineshaftGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////          //  if (this.settings.useVillages) this.villageGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////            if (this.settings.useStrongholds) this.strongholdGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////          //  if (this.settings.useTemples) this.scatteredFeatureGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////           // if (this.settings.useMonuments) this.oceanMonumentGenerator.generate(this.worldObj, x, z, chunkprimer)
    //////        }
    ////        val chunk: Chunk = new Chunk(world, chunkprimer, x, z)
    ////        val abyte: Array[Byte] = chunk.getBiomeArray
    ////        var i: Int = 0
    ////        while (i < abyte.length) {
    ////            {
    ////                abyte(i) = Biome.getIdForBiome(this.biomesForGeneration(i)).toByte
    ////            }
    ////            {
    ////                i += 1; i
    ////            }
    ////        }
    ////        chunk.generateSkylightMap()
    ////        chunk
    //    }

    override def populate(chunkX: Int, chunkY: Int, chunkZ: Int): Unit = ???

    override def generateStructures(chunkIn: Chunk, chunkX: Int, chunkY: Int, chunkZ: Int): Boolean = ???

    override def recreateStructures(chunkIn: Chunk, chunkX: Int, chunkY: Int, chunkZ: Int): Unit = ???
}
