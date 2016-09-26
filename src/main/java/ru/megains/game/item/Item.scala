package ru.megains.game.item

import ru.megains.game.block.Block
import ru.megains.game.register.GameRegister
import ru.megains.renderer.texture.{TTextureRegister, TextureAtlas}


class Item(val name: String) {


    var maxStackSize: Int = 64


    var aTexture: TextureAtlas = _

    def registerTexture(textureRegister: TTextureRegister): Unit = {
        aTexture = textureRegister.registerTexture(name)
    }
}

object Item {


    def getItemById(id: Int): Item = GameRegister.getItemById(id)

    def getItemFromBlock(block: Block): Item = GameRegister.getItemFromBlock(block)

    def initItems(): Unit = {
        GameRegister.registerItem(1000, new Item("stick"))
    }

}
