package ru.megains.common.network.play.server


import ru.megains.common.item.ItemStack
import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketWindowItems extends Packet[INetHandlerPlayClient] {


    var windowId: Int = 0
    var itemStacks: Array[ItemStack] = _


    def this(windowIdIn: Int, stacks: Array[ItemStack]) {
        this()
        windowId = windowIdIn
        itemStacks = stacks
    }


    def readPacketData(buf: PacketBuffer) {
        windowId = buf.readUnsignedByte
        itemStacks = new Array[ItemStack](buf.readShort)
        for (i <- itemStacks.indices) {
            itemStacks(i) = buf.readItemStackFromBuffer
        }
    }


    def writePacketData(buf: PacketBuffer) {
        buf.writeByte(windowId)
        buf.writeShort(itemStacks.length)
        for (itemStack <- itemStacks) {
            buf.writeItemStackToBuffer(itemStack)
        }
    }

    def processPacket(handler: INetHandlerPlayClient) {
        handler.handleWindowItems(this)
    }
}
