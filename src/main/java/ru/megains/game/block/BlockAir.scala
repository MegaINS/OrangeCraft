package ru.megains.game.block

import ru.megains.engine.graph.renderer.texture.TTextureRegister
import ru.megains.game.blockdata.{BlockSize, BlockWorldPos, MultiBlockPos}


class BlockAir(name: String) extends Block(name) {


    override def registerTexture(textureRegister: TTextureRegister) {}

    override def getSelectedBoundingBox(BlockWorldPos: BlockWorldPos, offset: MultiBlockPos) = BlockSize.NULL_AABB

    override def isOpaqueCube = false

    override def isAir: Boolean = true
}
