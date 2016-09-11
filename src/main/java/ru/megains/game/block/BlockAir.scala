package ru.megains.game.block

import ru.megains.game.blockdata.{BlockSize, BlockWorldPos, MultiBlockPos}
import ru.megains.renderer.texture.TTextureRegister


class BlockAir(name: String) extends Block(name) {


    override def registerTexture(textureRegister: TTextureRegister) {}

    override def getSelectedBoundingBox(BlockWorldPos: BlockWorldPos, offset: MultiBlockPos) = BlockSize.NULL_AABB

    override def isOpaqueCube = false

    override def isAir: Boolean = true
}
