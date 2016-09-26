package ru.megains.game.world.chunk

import ru.megains.game.blockdata.BlockWorldPos
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.MultiBlocks
import ru.megains.game.world.World


class ChunkVoid(world: World, position: ChunkPosition) extends Chunk(world, position) {


    override def isAirBlockWorldCord(pos: BlockWorldPos): Boolean = true

    override def getBlockWorldCord(pos: BlockWorldPos): AMultiBlock = MultiBlocks.air

}
