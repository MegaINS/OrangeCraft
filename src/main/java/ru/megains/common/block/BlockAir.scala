package ru.megains.common.block

import ru.megains.client.renderer.texture.TTextureRegister
import ru.megains.common.block.blockdata.{BlockPos, BlockSize, MultiBlockPos}


class BlockAir(name: String) extends Block(name) {


    override def registerTexture(textureRegister: TTextureRegister) {}

    override def getSelectedBoundingBox(BlockWorldPos: BlockPos, offset: MultiBlockPos) = BlockSize.NULL_AABB

    override def isOpaqueCube = false

    override def isAir: Boolean = true
}
