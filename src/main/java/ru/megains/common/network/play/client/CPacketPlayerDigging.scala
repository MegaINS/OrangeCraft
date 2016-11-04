package ru.megains.common.network.play.client

import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.play.client.CPacketPlayerDigging.Action
import ru.megains.common.network.play.client.CPacketPlayerDigging.Action.Action
import ru.megains.common.network.{Packet, PacketBuffer}


class CPacketPlayerDigging() extends Packet[INetHandlerPlayServer] {
    var position: BlockPos = _
    var facing: BlockDirection = _
    var action: Action = _

    def this(actionIn: Action, posIn: BlockPos, facingIn: BlockDirection) {
        this()
        action = actionIn
        position = posIn
        facing = facingIn
    }

    /**
      * Reads the raw packet data from the data stream.
      */

    def readPacketData(buf: PacketBuffer) {

        action = Action.apply(buf.readByte())
        position = buf.readBlockPos
        facing = BlockDirection.DIRECTIONAL_BY_ID(buf.readByte())
    }

    /**
      * Writes the raw packet data to the data stream.
      */

    def writePacketData(buf: PacketBuffer) {
        buf.writeByte(action.id)
        buf.writeBlockPos(position)
        buf.writeByte(facing.id)
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerPlayServer) {
        handler.processPlayerDigging(this)
    }


}

object CPacketPlayerDigging {

    object Action extends Enumeration {
        type Action = Value
        val START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM, SWAP_HELD_ITEMS = Value
    }

}
