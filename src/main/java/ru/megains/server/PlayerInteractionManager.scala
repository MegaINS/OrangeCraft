package ru.megains.server

import ru.megains.common.EnumActionResult.EnumActionResult
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.item.{Item, ItemStack}
import ru.megains.common.network.play.server.SPacketBlockChange
import ru.megains.common.world.{GameType, World}
import ru.megains.common.{ActionResult, EnumActionResult}
import ru.megains.server.entity.EntityPlayerMP

class PlayerInteractionManager(world: World) {

    var thisPlayerMP: EntityPlayerMP = _
    var blockReachDistance: Double = 5.0d
    var gameType: GameType = GameType.CREATIVE
    var isDestroyingBlock: Boolean = false

    def setGameType(gameTypeIn: GameType): Unit = {
        gameType = gameTypeIn
        // gameTypeIn.configurePlayerCapabilities(this.thisPlayerMP.capabilities)
        //  thisPlayerMP.sendPlayerAbilities()
        //  thisPlayerMP.ocServer.playerList.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, thisPlayerMP))
        //theWorld.updateAllPlayersSleepingFlag()
    }






    def isCreative: Boolean = gameType.isCreative

    def onBlockClicked(pos: BlockPos, side: BlockDirection) {
        //        val event: PlayerInteractEvent.LeftClickBlock = net.minecraftforge.common.ForgeHooks.onLeftClickBlock(thisPlayerMP, pos, side, net.minecraftforge.common.ForgeHooks.rayTraceEyeHitVec(thisPlayerMP, getBlockReachDistance + 1))
        //        if (event.isCanceled) {
        //            // Restore block and te data
        //            thisPlayerMP.connection.sendPacket(new SPacketBlockChange(theWorld, pos))
        //            theWorld.notifyBlockUpdate(pos, theWorld.getBlockState(pos), theWorld.getBlockState(pos), 3)
        //            return
        //        }
        if (isCreative) /* if (!theWorld.extinguishFire(null.asInstanceOf[EntityPlayer], pos, side))*/ tryHarvestBlock(pos)
        //        else {
        //            val iblockstate: IBlockState = theWorld.getBlockState(pos)
        //            val block: Block = iblockstate.getBlock
        //            if (gameType.isAdventure) {
        //                if (gameType eq GameType.SPECTATOR) return
        //                if (!thisPlayerMP.isAllowEdit) {
        //                    val itemstack: ItemStack = thisPlayerMP.getHeldItemMainhand
        //                    if (itemstack == null) return
        //                    if (!itemstack.canDestroy(block)) return
        //                }
        //            }
        //            initialDamage = curblockDamage
        //            var f: Float = 1.0F
        //            if (!iblockstate.getBlock.isAir(iblockstate, theWorld, pos)) {
        //                if (event.getUseBlock ne net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) {
        //                    block.onBlockClicked(theWorld, pos, thisPlayerMP)
        //                    theWorld.extinguishFire(null.asInstanceOf[EntityPlayer], pos, side)
        //                }
        //                else {
        //                    // Restore block and te data
        //                    thisPlayerMP.connection.sendPacket(new SPacketBlockChange(theWorld, pos))
        //                    theWorld.notifyBlockUpdate(pos, theWorld.getBlockState(pos), theWorld.getBlockState(pos), 3)
        //                }
        //                f = iblockstate.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, pos)
        //            }
        //            if (event.getUseItem eq net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) {
        //                if (f >= 1.0F) {
        //                    // Restore block and te data
        //                    thisPlayerMP.connection.sendPacket(new SPacketBlockChange(theWorld, pos))
        //                    theWorld.notifyBlockUpdate(pos, theWorld.getBlockState(pos), theWorld.getBlockState(pos), 3)
        //                }
        //                return
        //            }
        //            if (!iblockstate.getBlock.isAir(iblockstate, theWorld, pos) && f >= 1.0F) tryHarvestBlock(pos)
        //            else {
        //                isDestroyingBlock = true
        //                destroyPos = pos
        //                val i: Int = (f * 10.0F).toInt
        //                theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId, pos, i)
        //                durabilityRemainingOnBlock = i
        //            }
        //        }
    }

    def tryHarvestBlock(pos: BlockPos): Boolean = {
        //  val exp: Int = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(theWorld, gameType, thisPlayerMP, pos)
        //  if (exp == -1) false
        //  else {
        // val iblockstate: IBlockState = theWorld.getBlockState(pos)
        //  val tileentity: TileEntity = theWorld.getTileEntity(pos)
        //  val block: Block = iblockstate.getBlock
        //            if ((block.isInstanceOf[BlockCommandBlock] || block.isInstanceOf[BlockStructure]) && !thisPlayerMP.func_189808_dh) {
        //                theWorld.notifyBlockUpdate(pos, iblockstate, iblockstate, 3)
        //                false
        //            }
        //            else {
        //  val stack: ItemStack = thisPlayerMP.getHeldItemMainhand
        //  if (stack != null && stack.item.onBlockStartBreak(stack, pos, thisPlayerMP)) return false
        //    theWorld.playEvent(thisPlayerMP, 2001, pos, Block.getStateId(iblockstate))
        var flag1: Boolean = false
        if (isCreative) {
            flag1 = removeBlock(pos)
            thisPlayerMP.connection.sendPacket(new SPacketBlockChange(world, pos))
        }
        //                else {
        //                    val itemstack1: ItemStack = thisPlayerMP.getHeldItemMainhand
        //                    val itemstack2: ItemStack = if (itemstack1 == null) null
        //                    else itemstack1.copy
        //                    val flag: Boolean = iblockstate.getBlock.canHarvestBlock(theWorld, pos, thisPlayerMP)
        //                    if (itemstack1 != null) {
        //                        itemstack1.onBlockDestroyed(theWorld, iblockstate, pos, thisPlayerMP)
        //                        if (itemstack1.stackSize <= 0) {
        //                            net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, itemstack1, EnumHand.MAIN_HAND)
        //                            thisPlayerMP.setHeldItem(EnumHand.MAIN_HAND, null.asInstanceOf[ItemStack])
        //                        }
        //                    }
        //                    flag1 = removeBlock(pos, flag)
        //                    if (flag1 && flag) iblockstate.getBlock.harvestBlock(theWorld, thisPlayerMP, pos, iblockstate, tileentity, itemstack2)
        //                }
        // Drop experience
        //  if (!isCreative && flag1 && exp > 0) iblockstate.getBlock.dropXpOnBlockBreak(theWorld, pos, exp)
        flag1
        //   }
        //  }
    }

    private def removeBlock(pos: BlockPos): Boolean = removeBlock(pos, canHarvest = false)

    private def removeBlock(pos: BlockPos, canHarvest: Boolean): Boolean = {
        val block: Block = world.getBlock(pos)
        val flag: Boolean = block.removedByPlayer(world, pos, thisPlayerMP, canHarvest)
        if (flag) block.onBlockDestroyedByPlayer(world, pos)
        flag
    }

    def blockRemoving(pos: BlockPos) {
        //        if (pos == destroyPos) {
        //            val i: Int = curblockDamage - initialDamage
        //            val iblockstate: IBlockState = theWorld.getBlockState(pos)
        //            if (!iblockstate.getBlock.isAir(iblockstate, theWorld, pos)) {
        //                val f: Float = iblockstate.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, pos) * (i + 1).toFloat
        //                if (f >= 0.7F) {
        //                    isDestroyingBlock = false
        //                    theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId, pos, -1)
        //                    tryHarvestBlock(pos)
        //                }
        //                else if (!receivedFinishDiggingPacket) {
        //                    isDestroyingBlock = false
        //                    receivedFinishDiggingPacket = true
        //                    delayedDestroyPos = pos
        //                    initialBlockDamage = initialDamage
        //                }
        //            }
        //        }
    }


    def cancelDestroyingBlock() {
        isDestroyingBlock = false
        //  world.sendBlockBreakProgress(thisPlayerMP.getEntityId, destroyPos, -1)
    }

    def getBlockReachDistance: Double = blockReachDistance

    def processRightClick(player: EntityPlayer, worldIn: World, stack: ItemStack): EnumActionResult = {
        //  if (this.gameType eq GameType.SPECTATOR) EnumActionResult.PASS
        //   else
        //  if (player.getCooldownTracker.hasCooldown(stack.item)) EnumActionResult.PASS
        //   else {
        val i: Int = stack.stackSize
        // val j: Int = stack.getMetadata
        val actionresult: ActionResult[ItemStack] = stack.useItemRightClick(worldIn, player)
        val itemstack: ItemStack = actionresult.result.asInstanceOf[ItemStack]
        //  if ((itemstack eq stack) && itemstack.stackSize == i && itemstack.getMaxItemUseDuration <= 0 && itemstack.getMetadata == j) actionresult.`type`
        //  else {
        player.setHeldItem(itemstack)
        if (this.isCreative) {
            itemstack.stackSize = i
            //  if (itemstack.isItemStackDamageable) itemstack.setItemDamage(j)
        }
        if (itemstack.stackSize == 0) {
            player.setHeldItem(null)

        }
        //  if (!player.isHandActive) player.asInstanceOf[EntityPlayerMP].sendContainerToPlayer(player.inventoryContainer)
        actionresult.`type`
        //   }
        //  }
    }

    def processRightClickBlock(player: EntityPlayer, worldIn: World, stack: ItemStack, posMouseOver: BlockPos, posBlockSet: BlockPos, facing: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult = {
        //        if (this.gameType eq GameType.SPECTATOR) {
        //            val tileentity: TileEntity = worldIn.getTileEntity(pos)
        //            if (tileentity.isInstanceOf[ILockableContainer]) {
        //                val block1: Block = worldIn.getBlockState(pos).getBlock
        //                var ilockablecontainer: ILockableContainer = tileentity.asInstanceOf[ILockableContainer]
        //                if (ilockablecontainer.isInstanceOf[TileEntityChest] && block1.isInstanceOf[BlockChest]) ilockablecontainer = block1.asInstanceOf[BlockChest].getLockableContainer(worldIn, pos)
        //                if (ilockablecontainer != null) {
        //                    player.displayGUIChest(ilockablecontainer)
        //                    return EnumActionResult.SUCCESS
        //                }
        //            }
        //            else if (tileentity.isInstanceOf[IInventory]) {
        //                player.displayGUIChest(tileentity.asInstanceOf[IInventory])
        //                return EnumActionResult.SUCCESS
        //            }
        //            EnumActionResult.PASS
        //        }
        //        else {

        val item: Item = if (stack == null) null
        else stack.item
        val ret: EnumActionResult = if (item == null) EnumActionResult.PASS
        else item.onItemUseFirst(stack, player, worldIn, posMouseOver, facing, hitX, hitY, hitZ)
        if (ret ne EnumActionResult.PASS) return ret
        val bypass: Boolean = true
        ///   for (s <- Array[ItemStack](player.getHeldItemMainhand, player.getHeldItemOffhand))
        //    bypass = bypass && (s == null || s.item.doesSneakBypassUse(s, worldIn, pos, player))
        var result: EnumActionResult = EnumActionResult.PASS
        if (!player.isSneaking || bypass) {
            val block: Block = worldIn.getBlock(posMouseOver)
            if (block.onBlockActivated(worldIn, posMouseOver, player, stack, facing, hitX, hitY, hitZ)) result = EnumActionResult.SUCCESS
        }
        if (stack == null) EnumActionResult.PASS
        // else if (player.getCooldownTracker.hasCooldown(stack.item)) EnumActionResult.PASS
        else {
            //                if (stack.item.isInstanceOf[ItemBlock] && !player.func_189808_dh) {
            //                    val block: Block = stack.item.asInstanceOf[ItemBlock].getBlock
            //                    if (block.isInstanceOf[BlockCommandBlock] || block.isInstanceOf[BlockStructure]) return EnumActionResult.FAIL
            //                }
            if (isCreative) {
                //  val j: Int = stack.getMetadata
                val i: Int = stack.stackSize

                val enumactionresult: EnumActionResult = stack.onItemUse(player, worldIn, posBlockSet, facing, hitX, hitY, hitZ)
                //  stack.setItemDamage(j)
                stack.stackSize = i
                enumactionresult

            } else stack.onItemUse(player, worldIn, posBlockSet, facing, hitX, hitY, hitZ)

        }
        // }
    }


}

