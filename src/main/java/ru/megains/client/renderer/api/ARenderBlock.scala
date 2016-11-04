package ru.megains.client.renderer.api

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.common.world.World


abstract class ARenderBlock {

    def render(block: Block, world: World, posWorld: BlockPos, posRender: BlockPos, offset: MultiBlockPos = MultiBlockPos.default): Boolean
}
