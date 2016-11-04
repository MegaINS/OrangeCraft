package ru.megains.common.item

import ru.megains.common.EnumActionResult
import ru.megains.common.EnumActionResult.EnumActionResult
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.world.World

class ItemBlock(val block: Block) extends Item(block.name) {


    override def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, facing: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = {


        //   val block: Block = worldIn.getBlock(pos)
        // if (!block.isReplaceable(worldIn, pos)) pos = pos.offset(facing)
        if (stack.stackSize != 0 && /* playerIn.canPlayerEdit(pos, facing, stack)*//* &&*/ worldIn.canBlockBePlaced(this.block, pos, false, facing, null, stack)) {
            //  val i: Int = getMetadata(stack.getMetadata)
            // val iblockstate1: IBlockState = block.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn)
            if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ)) {
                //   val soundtype: SoundType = block.getSoundType
                //  worldIn.playSound(playerIn, pos, soundtype.getPlaceSound, SoundCategory.BLOCKS, (soundtype.getVolume + 1.0F) / 2.0F, soundtype.getPitch * 0.8F)
                stack.stackSize -= 1
            }
            EnumActionResult.SUCCESS
        }
        else EnumActionResult.FAIL
    }

    def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
        if (!world.setBlock(pos, block, 3)) return false

        // setTileEntityNBT(world, player, pos, stack)
        block.onBlockPlacedBy(world, pos, player, stack)
        true
    }

    def canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: BlockDirection, player: EntityPlayer, stack: ItemStack): Boolean = {
        val block: Block = worldIn.getBlock(pos)
        //  if ((block eq Blocks.SNOW_LAYER) && block.isReplaceable(worldIn, pos)) side = EnumFacing.UP
        //  else if (!block.isReplaceable(worldIn, pos)) pos = pos.offset(side)
        worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack)
    }
}
