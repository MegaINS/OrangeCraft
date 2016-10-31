package ru.megains.game.world.chunk

import ru.megains.game.block.Block
import ru.megains.game.blockdata.BlockPos
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.MultiBlocks
import ru.megains.game.world.World


class ChunkVoid(world: World, position: ChunkPosition) extends Chunk(world, position) {


    override def isAirBlockWorldCord(pos: BlockPos): Boolean = true

    override def getBlockWorldCord(pos: BlockPos): AMultiBlock = MultiBlocks.air

    override def setBlockChunkCord(pos: BlockPos, block: Block): Boolean = false

}
