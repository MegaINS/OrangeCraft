package ru.megains.server.world


import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.world.{IWorldEventListener, World}
import ru.megains.server.OrangeCraftServer

class ServerWorldEventHandler(server: OrangeCraftServer, world: WorldServer) extends IWorldEventListener {
    override def notifyBlockUpdate(worldIn: World, pos: BlockPos, newState: Block, flags: Int): Unit = {

        world.playerManager.markBlockForUpdate(pos)

    }
}
