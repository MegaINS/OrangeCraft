package ru.megains.common.network

abstract class Packet[T <: INetHandler] {


    def readPacketData(buf: PacketBuffer): Unit

    def writePacketData(buf: PacketBuffer): Unit

    def processPacket(handler: T): Unit

}



