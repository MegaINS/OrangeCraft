package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}

class SPacketChangeGameState extends Packet[INetHandlerPlayClient] {


    var state: Int = 0
    var value: Int = 0

    def this(stateIn: Int, valueIn: Int) {
        this()
        this.state = stateIn
        this.value = valueIn
    }

    override def readPacketData(buf: PacketBuffer): Unit = {
        state = buf.readUnsignedByte
        value = buf.readUnsignedByte
    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeByte(state)
        buf.writeByte(value)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handleChangeGameState(this)
    }


}
