package ru.megains.game.item

import org.joml.Vector3f
import ru.megains.game.block.Block
import ru.megains.game.register.GameRegister
import ru.megains.renderer.texture.{TTextureRegister, TextureAtlas}


class Item(val name: String) {


    var aTexture: TextureAtlas = _

    def registerTexture(textureRegister: TTextureRegister): Unit = {
        aTexture = textureRegister.registerTexture(name)
    }
}

object Item {

    val SCALE: Float = 25f
    val ROTATION = new Vector3f(-25, 45, 0)

    def getItemById(id: Int): Item = GameRegister.getItemById(id)

    def getItemFromBlock(block: Block): Item = GameRegister.getItemFromBlock(block)

    def initItems(): Unit = {
        GameRegister.registerItem(1000, new Item("stick"))
    }

}
