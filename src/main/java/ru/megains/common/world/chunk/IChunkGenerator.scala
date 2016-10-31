package ru.megains.common.world.chunk

import ru.megains.game.world.chunk.Chunk

trait IChunkGenerator {

    def provideChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk

    def populate(chunkX: Int, chunkY: Int, chunkZ: Int)

    def generateStructures(chunkIn: Chunk, chunkX: Int, chunkY: Int, chunkZ: Int): Boolean

    // def getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): util.List[Biome.SpawnListEntry]

    // def getStrongholdGen(worldIn: World, structureName: String, position: BlockPos): BlockPos

    def recreateStructures(chunkIn: Chunk, chunkX: Int, chunkY: Int, chunkZ: Int)
}
