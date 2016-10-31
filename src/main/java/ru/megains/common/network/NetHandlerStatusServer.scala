package ru.megains.common.network

import ru.megains.common.network.status.INetHandlerStatusServer
import ru.megains.common.network.status.client.{CPacketPing, CPacketServerQuery}
import ru.megains.common.network.status.server.{SPacketPong, SPacketServerInfo}
import ru.megains.server.OrangeCraftServer


class NetHandlerStatusServer(val server: OrangeCraftServer, val networkManager: NetworkManager) extends INetHandlerStatusServer {
    private var handled: Boolean = false

    /**
      * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
      */
    def onDisconnect(reason: String) {
    }

    def processServerQuery(packetIn: CPacketServerQuery) {

        if (handled) this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE)
        else {
            handled = true
            networkManager.sendPacket(new SPacketServerInfo(server.getServerStatusResponse))
        }
    }

    def processPing(packetIn: CPacketPing) {

        this.networkManager.sendPacket(new SPacketPong(packetIn.getClientTime))
        this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE)
    }

}

object NetHandlerStatusServer {

    private val EXIT_MESSAGE: String = "Status request has been handled."
}


