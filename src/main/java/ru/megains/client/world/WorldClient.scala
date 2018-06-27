package ru.megains.client.world

import ru.megains.client.renderer.world.{SaveHandlerMP, WorldRenderer}
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.network.NetHandlerPlayClient
import ru.megains.common.position.ChunkPosition
import ru.megains.common.world.{ChunkProviderClient, IChunkProvider, World}

class WorldClient(connection: NetHandlerPlayClient) extends World(new SaveHandlerMP) {


    override var isRemote: Boolean = true
    var worldRenderer: WorldRenderer = _

    def doPreChunk(pos: ChunkPosition, loadChunk: Boolean) = {
        if (loadChunk) chunkProvider.loadChunk(pos)
        else {
            //  this.chunkProvider.unloadChunk(chunkX, chunkZ)
            //    this.markBlockRangeForRenderUpdate(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 256, chunkZ * 16 + 15)
        }
    }

    def invalidateRegionAndSetBlock(pos: BlockPos, block: Block, meta: Int): Boolean = {
        //  val i: Int = pos.getX
        //  val j: Int = pos.getY
        //   val k: Int = pos.getZ
        // invalidateBlockReceiveRegion(i, j, k, i, j, k)
        val isOk = setBlock(pos, block, 3)
        if (isOk) {
            setBlockMeta(pos, meta)
        }
        isOk
    }


    override val chunkProvider: IChunkProvider = new ChunkProviderClient(this)

    override def save(): Unit = {}

    override def setBlock(pos: BlockPos, block: Block, flag: Int): Boolean = {
        if (super.setBlock(pos, block, flag)) {
            worldRenderer.reRender(pos)
            true
        } else {
            false
        }
    }

}
