package ru.megains.common.network

import java.util.function.Consumer

import com.google.common.collect.{BiMap, HashBiMap}
import ru.megains.common.network.PacketDirection.PacketDirection
import ru.megains.common.network.handshake.client.CHandshake
import ru.megains.common.network.login.client.CPacketLoginStart
import ru.megains.common.network.login.server.{SPacketDisconnect, SPacketLoginSuccess}
import ru.megains.common.network.play.client._
import ru.megains.common.network.play.server._
import ru.megains.common.network.status.client.{CPacketPing, CPacketServerQuery}
import ru.megains.common.network.status.server.{SPacketPong, SPacketServerInfo}

import scala.collection.mutable


sealed abstract class ConnectionState(val name: String, val id: Int) {


    val directionMap = new mutable.HashMap[PacketDirection, BiMap[Int, Class[_ <: Packet[_]]]]()
    directionMap += PacketDirection.SERVERBOUND -> HashBiMap.create()
    directionMap += PacketDirection.CLIENTBOUND -> HashBiMap.create()


    def registerPacket(direction: PacketDirection, packet: Class[_ <: Packet[_]]): Unit = {


        val biMap = directionMap(direction)
        biMap.put(biMap.size(), packet)
        // ConnectionState.STATES_BY_CLASS += packet -> this

    }

    def getPacketId(direction: PacketDirection, packet: Class[_ <: Packet[_]]): Int = {
        directionMap(direction).inverse().get(packet)
    }


    def getPacket(direction: PacketDirection, id: Int): Packet[_] = {
        val packet = directionMap(direction).get(id)
        if (packet ne null) packet.newInstance() else null.asInstanceOf[Packet[_]]
    }

}


object ConnectionState {


    val STATES_BY_CLASS = new mutable.HashMap[Class[_ <: Packet[_]], ConnectionState]()

    def getFromId(id: Int): ConnectionState = {
        STATES_BY_ID(id)
    }


    def getFromPacket(inPacket: Packet[_]): ConnectionState = {
        STATES_BY_CLASS(inPacket.getClass)
    }


    case object HANDSHAKING extends ConnectionState("HANDSHAKING", 0) {
        registerPacket(PacketDirection.SERVERBOUND, classOf[CHandshake])
    }


    case object STATUS extends ConnectionState("STATUS", 1) {
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketPong])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketServerInfo])

        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPing])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketServerQuery])
    }

    case object LOGIN extends ConnectionState("LOGIN", 2) {
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketDisconnect])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketLoginSuccess])

        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketLoginStart])

    }


    case object PLAY extends ConnectionState("PLAY", 3) {
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketBlockChange])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketChunkData])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketHeldItemChange])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketJoinGame])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketPlayerPosLook])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketSpawnPosition])
        registerPacket(PacketDirection.CLIENTBOUND, classOf[SPacketUnloadChunk])




        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketHeldItemChange])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayer])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayer.Position])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayer.Rotation])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayer.PositionRotation])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayerDigging])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayerTryUseItem])
        registerPacket(PacketDirection.SERVERBOUND, classOf[CPacketPlayerTryUseItemOnBlock])


    }

    val STATES_BY_ID = Array(HANDSHAKING, STATUS, LOGIN, PLAY)
    addClass(HANDSHAKING)
    addClass(STATUS)
    addClass(LOGIN)
    addClass(PLAY)


    def addClass(state: ConnectionState): Unit = {
        state.directionMap.values.foreach(
            _.values().forEach(
                new Consumer[Class[_ <: Packet[_]]] {
                    override def accept(packet: Class[_ <: Packet[_]]): Unit = {
                        STATES_BY_CLASS += packet -> state
                    }
                }
            )
        )
    }


}
