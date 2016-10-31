package ru.megains.client.renderer.api

import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.game.world.World


abstract class ARenderBlock {

    def render(block: Block, world: World, posWorld: BlockPos, posRender: BlockPos, offset: MultiBlockPos = MultiBlockPos.default): Boolean
}
