package ru.megains.common.world

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos

trait IWorldEventListener {
    def notifyBlockUpdate(worldIn: World, pos: BlockPos, newState: Block, flags: Int)

    //  def notifyLightSet(pos: BlockPos)

    /**
      * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing.
      */
    //   def markBlockRangeForRenderUpdate(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int)

    // def playSoundToAllNearExcept(@Nullable player: EntityPlayer, soundIn: SoundEvent, category: SoundCategory, x: Double, y: Double, z: Double, volume: Float, pitch: Float)

    // def playRecord(soundIn: SoundEvent, pos: BlockPos)

    //   def spawnParticle(particleID: Int, ignoreRange: Boolean, xCoord: Double, yCoord: Double, zCoord: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, parameters: Int*)

    /**
      * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any
      * necessary textures. On server worlds, adds the entity to the entity tracker.
      */
    //  def onEntityAdded(entityIn: Entity)

    /**
      * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
      * textures. On server worlds, removes the entity from the entity tracker.
      */
    //  def onEntityRemoved(entityIn: Entity)

    //   def broadcastSound(soundID: Int, pos: BlockPos, data: Int)

    //  def playEvent(player: EntityPlayer, `type`: Int, blockPosIn: BlockPos, data: Int)

    //  def sendBlockBreakProgress(breakerId: Int, pos: BlockPos, progress: Int)
}
