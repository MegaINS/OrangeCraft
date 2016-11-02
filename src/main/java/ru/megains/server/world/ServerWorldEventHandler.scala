package ru.megains.server.world


import ru.megains.game.block.Block
import ru.megains.game.blockdata.BlockPos
import ru.megains.game.world.{IWorldEventListener, World}
import ru.megains.server.OrangeCraftServer

class ServerWorldEventHandler(server: OrangeCraftServer, world: WorldServer) extends IWorldEventListener {
    override def notifyBlockUpdate(worldIn: World, pos: BlockPos, newState: Block, flags: Int): Unit = {

        world.playerManager.markBlockForUpdate(pos)

    }
}
