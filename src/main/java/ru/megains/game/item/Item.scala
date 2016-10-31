package ru.megains.game.item

import ru.megains.client.renderer.texture.{TTextureRegister, TextureAtlas}
import ru.megains.common.EnumActionResult.EnumActionResult
import ru.megains.common.{ActionResult, EnumActionResult}
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockPos}
import ru.megains.game.entity.player.EntityPlayer
import ru.megains.game.register.GameRegister
import ru.megains.game.world.World


class Item(val name: String) {


    var maxStackSize: Int = 64


    var aTexture: TextureAtlas = _

    def registerTexture(textureRegister: TTextureRegister): Unit = {
        aTexture = textureRegister.registerTexture(name)
    }

    def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, facing: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = EnumActionResult.PASS

    def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = EnumActionResult.PASS

    def onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer): ActionResult[ItemStack] = new ActionResult[ItemStack](EnumActionResult.PASS, itemStackIn)
}

object Item {


    def getItemById(id: Int): Item = GameRegister.getItemById(id)

    def getItemFromBlock(block: Block): Item = GameRegister.getItemFromBlock(block)

    def initItems(): Unit = {
        GameRegister.registerItem(1000, new Item("stick"))
    }

}
