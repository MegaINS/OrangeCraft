package ru.megains.common.network.play.server

import ru.megains.common.network.{Packet, PacketBuffer}

import scala.collection.immutable.HashSet

class SPacketPlayerPosLook extends Packet[INetHandlerPlayClient] {


    var x: Double = 0

    var y: Double = 0

    var z: Double = 0

    var yaw: Float = 0

    var pitch: Float = 0

    var flags: HashSet[SPacketPlayerPosLook.EnumFlags.EnumFlags] = _

    var teleportId: Int = 0

    def this(xIn: Double, yIn: Double, zIn: Double, yawIn: Float, pitchIn: Float, flagsIn: HashSet[SPacketPlayerPosLook.EnumFlags.EnumFlags], teleportIdIn: Int) {
        this()
        x = xIn
        y = yIn
        z = zIn
        yaw = yawIn
        pitch = pitchIn
        flags = flagsIn
        teleportId = teleportIdIn

    }

    override def readPacketData(buf: PacketBuffer): Unit = {
        x = buf.readDouble
        y = buf.readDouble
        z = buf.readDouble
        yaw = buf.readFloat
        pitch = buf.readFloat
        flags = SPacketPlayerPosLook.EnumFlags.unpack(buf.readUnsignedByte)
        teleportId = buf.readVarIntFromBuffer
    }

    override def writePacketData(buf: PacketBuffer): Unit = {
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeFloat(yaw)
        buf.writeFloat(pitch)
        buf.writeByte(SPacketPlayerPosLook.EnumFlags.pack(flags))
        buf.writeVarIntToBuffer(teleportId)
    }

    override def processPacket(handler: INetHandlerPlayClient): Unit = {
        handler.handlePlayerPosLook(this)
    }
}

object SPacketPlayerPosLook {

    object EnumFlags extends Enumeration {
        def pack(flags: HashSet[EnumFlags]): Int = {

            var out = 0
            flags.foreach(
                (flag) => {
                    out |= 1 << flag.id
                }
            )
            out
        }


        def unpack(data: Int): HashSet[EnumFlags] = {
            var out = new HashSet[EnumFlags]
            for (i <- 0 to 5) {
                if ((data >> i & 1) == 1) {
                    out += this (i)
                }
            }
            out
        }

        type EnumFlags = Value
        val X, Y, Z, Y_ROT, X_ROT, ACCEPTED = Value
    }

}
