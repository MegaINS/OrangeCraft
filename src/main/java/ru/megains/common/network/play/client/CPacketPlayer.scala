package ru.megains.common.network.play.client

import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.{Packet, PacketBuffer}

class CPacketPlayer extends Packet[INetHandlerPlayServer] {


    var x: Double = .0
    var y: Double = .0
    var z: Double = .0
    var yaw: Float = .0f
    var pitch: Float = .0f
    var onGround: Boolean = false
    var moving: Boolean = false
    var rotating: Boolean = false

    def this(onGroundIn: Boolean) {
        this()
        onGround = onGroundIn
    }


    override def readPacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def writePacketData(packetBuffer: PacketBuffer): Unit = {

    }

    override def processPacket(handler: INetHandlerPlayServer): Unit = {
        handler.processPlayer(this)
    }


    def getX(posX: Double): Double = if (moving) x else posX

    def getY(posY: Double): Double = if (moving) y else posY

    def getZ(posZ: Double): Double = if (moving) z else posZ

    def getYaw(rotationYaw: Float): Float = if (rotating) yaw else rotationYaw

    def getPitch(rotationPitch: Float): Float = if (rotating) pitch else rotationPitch

}

object CPacketPlayer {

    class Position() extends CPacketPlayer {

        moving = true

        def this(xIn: Double, yIn: Double, zIn: Double, onGroundIn: Boolean) {
            this()
            x = xIn
            y = yIn
            z = zIn
            onGround = onGroundIn
            moving = true
        }


        override def readPacketData(buf: PacketBuffer) {
            x = buf.readDouble
            y = buf.readDouble
            z = buf.readDouble
            super.readPacketData(buf)
        }


        override def writePacketData(buf: PacketBuffer) {
            buf.writeDouble(x)
            buf.writeDouble(y)
            buf.writeDouble(z)
            super.writePacketData(buf)
        }
    }


    class PositionRotation() extends CPacketPlayer {
        moving = true
        rotating = true

        def this(xIn: Double, yIn: Double, zIn: Double, yawIn: Float, pitchIn: Float, onGroundIn: Boolean) {
            this()
            x = xIn
            y = yIn
            z = zIn
            yaw = yawIn
            pitch = pitchIn
            onGround = onGroundIn
            rotating = true
            moving = true
        }


        override def readPacketData(buf: PacketBuffer) {
            x = buf.readDouble
            y = buf.readDouble
            z = buf.readDouble
            yaw = buf.readFloat
            pitch = buf.readFloat
            super.readPacketData(buf)
        }


        override def writePacketData(buf: PacketBuffer) {
            buf.writeDouble(x)
            buf.writeDouble(y)
            buf.writeDouble(z)
            buf.writeFloat(yaw)
            buf.writeFloat(pitch)
            super.writePacketData(buf)
        }
    }

    class Rotation() extends CPacketPlayer {
        rotating = true

        def this(yawIn: Float, pitchIn: Float, onGroundIn: Boolean) {
            this()
            yaw = yawIn
            pitch = pitchIn
            onGround = onGroundIn
            rotating = true
        }


        override def readPacketData(buf: PacketBuffer) {
            yaw = buf.readFloat
            pitch = buf.readFloat
            super.readPacketData(buf)
        }

        override def writePacketData(buf: PacketBuffer) {
            buf.writeFloat(yaw)
            buf.writeFloat(pitch)
            super.writePacketData(buf)
        }
    }

}


