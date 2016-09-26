package ru.megains.renderer.api

import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockWorldPos, MultiBlockPos}
import ru.megains.game.world.World


abstract class ARenderBlock {

    def render(block: Block, world: World, posWorld: BlockWorldPos, posRender: BlockWorldPos, offset: MultiBlockPos = MultiBlockPos.default): Boolean
}
