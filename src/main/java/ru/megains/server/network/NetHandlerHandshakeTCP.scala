package ru.megains.server.network

import ru.megains.common.network.ConnectionState.{LOGIN, STATUS}
import ru.megains.common.network.handshake.client.CHandshake
import ru.megains.common.network.login.server.SPacketDisconnect
import ru.megains.common.network.{NetHandlerLoginServer, NetHandlerStatusServer, NetworkManager}
import ru.megains.server.OrangeCraftServer
import ru.megains.server.network.handshake.INetHandlerHandshakeServer

class NetHandlerHandshakeTCP(server: OrangeCraftServer, networkManager: NetworkManager) extends INetHandlerHandshakeServer {


    override def processHandshake(packetIn: CHandshake): Unit = {

        packetIn.connectionState match {
            case LOGIN =>
                networkManager.setConnectionState(LOGIN)
                if (packetIn.version > 210) {
                    val text: String = "Outdated server! I\'m still on 1.10.2"
                    networkManager.sendPacket(new SPacketDisconnect(text))
                    networkManager.closeChannel(text)
                }
                else if (packetIn.version < 210) {
                    val text: String = "Outdated client! Please use 1.10.2"
                    networkManager.sendPacket(new SPacketDisconnect(text))
                    networkManager.closeChannel(text)
                }
                else networkManager.setNetHandler(new NetHandlerLoginServer(server, networkManager))

            case STATUS =>
                networkManager.setConnectionState(STATUS)
                networkManager.setNetHandler(new NetHandlerStatusServer(server, networkManager))

            case _ =>
                throw new UnsupportedOperationException("Invalid intention " + packetIn.connectionState)
        }
    }

    override def onDisconnect(msg: String): Unit = {

    }


}
