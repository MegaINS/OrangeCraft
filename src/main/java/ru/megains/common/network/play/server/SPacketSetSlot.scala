package ru.megains.common.network.play.server

import ru.megains.common.item.ItemStack
import ru.megains.common.network.{Packet, PacketBuffer}


class SPacketSetSlot extends Packet[INetHandlerPlayClient] {

    var windowId: Int = 0
    var slot: Int = 0
    var item: ItemStack = _


    def this(windowIdIn: Int, slotIn: Int, itemIn: ItemStack) {
        this()
        windowId = windowIdIn
        slot = slotIn
        item = if (itemIn == null) null else itemIn
    }


    def processPacket(handler: INetHandlerPlayClient) {
        handler.handleSetSlot(this)
    }


    def readPacketData(buf: PacketBuffer) {
        windowId = buf.readByte
        slot = buf.readShort
        item = buf.readItemStackFromBuffer
    }


    def writePacketData(buf: PacketBuffer) {
        buf.writeByte(windowId)
        buf.writeShort(slot)
        buf.writeItemStackToBuffer(item)
    }
}
