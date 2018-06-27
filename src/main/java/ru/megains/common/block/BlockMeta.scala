package ru.megains.common.block

import ru.megains.client.renderer.texture.{TTextureRegister, TextureAtlas}
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.entity.EntityLivingBase
import ru.megains.common.item.ItemStack
import ru.megains.common.world.World

class BlockMeta(name: String) extends Block(name) {

    val textures: Array[TextureAtlas] = new Array[TextureAtlas](6)

    override def registerTexture(textureRegister: TTextureRegister): Unit = {
        for (i <- textures.indices) {
            textures(i) = textureRegister.registerTexture((i + 1).toString)
        }
    }

    override def getATexture(pos: BlockPos, blockDirection: BlockDirection, world: World): TextureAtlas = {
        blockDirection.id match {
            case 0 | 1 => textures(blockDirection.id)
            case id =>
                var i = id + world.getBlockMeta(pos)
                if (i > 5) i -= 5
                textures(i)
        }

    }

    override def getATexture(blockDirection: BlockDirection): TextureAtlas = {
        textures(blockDirection.id)
    }

    override def onBlockPlacedBy(worldIn: World, pos: BlockPos, placer: EntityLivingBase, stack: ItemStack): Unit = {
        worldIn.setBlockMeta(pos, placer.side.id - 2)
    }
}
