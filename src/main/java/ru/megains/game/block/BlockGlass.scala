package ru.megains.game.block

import ru.megains.engine.graph.renderer.texture.{TextureAtlas, ATexture}
import ru.megains.game.blockdata.BlockDirection


class BlockGlass(name:String) extends Block(name){




    override def isOpaqueCube: Boolean = false

}
