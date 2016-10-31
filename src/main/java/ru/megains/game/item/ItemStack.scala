package ru.megains.game.item

import ru.megains.common.ActionResult
import ru.megains.common.EnumActionResult.EnumActionResult
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockPos}
import ru.megains.game.entity.player.EntityPlayer
import ru.megains.game.world.World

class ItemStack(val item: Item, var stackSize: Int) {


    def this(block: Block, stackSize: Int) = this(Item.getItemFromBlock(block), stackSize)

    def this(block: Block) = this(block, 1)

    def this(item: Item) = this(item, 1)


    def splitStack(size: Int): ItemStack = {
        stackSize -= size
        new ItemStack(item, size)
    }

    def onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos, side: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = {
        // if (!worldIn.isRemote) return net.minecraftforge.common.ForgeHooks.onPlaceItemIntoWorld(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ)
        val enumactionresult: EnumActionResult = item.onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ)
        //  if (enumactionresult eq EnumActionResult.SUCCESS) playerIn.addStat(StatList.getObjectUseStats(this.item))
        enumactionresult
    }

    def useItemRightClick(worldIn: World, playerIn: EntityPlayer): ActionResult[ItemStack] = item.onItemRightClick(this, worldIn, playerIn)



}
