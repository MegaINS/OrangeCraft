package ru.megains.common.world.chunk

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.multiblock.AMultiBlock
import ru.megains.common.position.ChunkPosition
import ru.megains.common.register.MultiBlocks
import ru.megains.common.world.World


class ChunkVoid(world: World, position: ChunkPosition) extends Chunk(world, position) {


    override def isAirBlockWorldCord(pos: BlockPos): Boolean = true

    override def getMultiBlockWorldCord(pos: BlockPos): AMultiBlock = MultiBlocks.air

    override def setBlockChunkCord(pos: BlockPos, block: Block): Boolean = false

}
