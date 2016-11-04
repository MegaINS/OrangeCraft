package ru.megains.client.network

import java.net.{InetAddress, UnknownHostException}

import ru.megains.client.OrangeCraft
import ru.megains.common.network.handshake.client.CHandshake
import ru.megains.common.network.status.INetHandlerStatusClient
import ru.megains.common.network.status.client.{CPacketPing, CPacketServerQuery}
import ru.megains.common.network.status.server.{SPacketPong, SPacketServerInfo}
import ru.megains.common.network.{ConnectionState, NetworkManager}
import ru.megains.common.utils.Logger

class ServerPinger extends Logger[ServerPinger] {

    //  private val pingDestinations: util.List[NetworkManager] = Collections.synchronizedList[NetworkManager](Lists.newArrayList[NetworkManager])

    @throws[UnknownHostException]
    def ping(server: ServerData) {
        val serveraddress: ServerAddress = new ServerAddress(server.serverIP, 20000)
        // val networkmanager: NetworkManager = NetworkManager.createLocalClient(LocalAddress.ANY)
        val networkmanager: NetworkManager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(serveraddress.getIP), serveraddress.getPort)
        // pingDestinations.add(networkmanager)
        server.serverMOTD = "Pinging..."
        server.pingToServer = -1L
        server.playerList = null
        networkmanager.setNetHandler(new INetHandlerStatusClient() {
            private var successful: Boolean = false
            private
            var receivedStatus: Boolean = false
            private
            var pingSentAt: Long = 0L

            override def handleServerInfo(packetIn: SPacketServerInfo) {
                if (receivedStatus) networkmanager.closeChannel("ServerPinger" + "receivedStatus")
                else {
                    receivedStatus = true
                    //                    val serverstatusresponse: ServerStatusResponse = packetIn.getResponse
                    //                    if (serverstatusresponse.getServerDescription != null) server.serverMOTD = serverstatusresponse.getServerDescription
                    //                    else server.serverMOTD = ""
                    //                    if (serverstatusresponse.getVersion != null) {
                    //                        server.gameVersion = serverstatusresponse.getVersion.getName
                    //                        server.version = serverstatusresponse.getVersion.getProtocol
                    //                    }
                    //                    else {
                    //                        server.gameVersion = "Old"
                    //                        server.version = 0
                    //                    }
                    //                    if (serverstatusresponse.getPlayers != null) {
                    //                        server.populationInfo =  "" + serverstatusresponse.getPlayers.getOnlinePlayerCount + "" + "/" + serverstatusresponse.getPlayers.getMaxPlayers
                    //                        if (ArrayUtils.isNotEmpty(serverstatusresponse.getPlayers.getPlayers)) {
                    //                            val stringbuilder: StringBuilder = new StringBuilder
                    //                            for (gameprofile <- serverstatusresponse.getPlayers.getPlayers) {
                    //                                if (stringbuilder.nonEmpty) stringbuilder.append("\n")
                    //                                stringbuilder.append(gameprofile.getName)
                    //                            }
                    //                            if (serverstatusresponse.getPlayers.getPlayers.length < serverstatusresponse.getPlayers.getOnlinePlayerCount) {
                    //                                if (stringbuilder.nonEmpty) stringbuilder.append("\n")
                    //                                stringbuilder.append("... and ").append(serverstatusresponse.getPlayers.getOnlinePlayerCount - serverstatusresponse.getPlayers.getPlayers.length).append(" more ...")
                    //                            }
                    //                            server.playerList = stringbuilder.toString
                    //                        }
                    //                    }
                    // else
                    //  server.populationInfo = "???"
                    //                    if (serverstatusresponse.getFavicon != null) {
                    //                        val s: String = serverstatusresponse.getFavicon
                    //                        if (s.startsWith("data:image/png;base64,")) server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length))
                    //                        else log.error("Invalid server icon (unknown format)")
                    //                    }
                    //  else server.setBase64EncodedIconData(null.asInstanceOf[String])
                    // net.minecraftforge.fml.client.FMLClientHandler.instance.bindServerListData(server, serverstatusresponse)
                    this.pingSentAt = OrangeCraft.getSystemTime()
                    networkmanager.sendPacket(new CPacketPing(pingSentAt))
                    this.successful = true
                }
            }

            override def handlePong(packetIn: SPacketPong) {
                val i: Long = pingSentAt
                val j: Long = OrangeCraft.getSystemTime()
                server.pingToServer = j - i
                networkmanager.closeChannel("ServerPinger" + "handlePong")
            }

            /**
              * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
              */
            override def onDisconnect(msg: String) {
                if (!successful) {
                    log.error("Can\'t ping {}: {}", Array[AnyRef](server.serverIP, msg))
                    server.serverMOTD = "Can\'t connect to server."
                    server.populationInfo = ""
                    // ServerPinger.tryCompatibilityPing(server)
                }
            }
        })
        try {
            networkmanager.sendPacket(new CHandshake(210, serveraddress.getIP, serveraddress.getPort, ConnectionState.STATUS))
            networkmanager.sendPacket(new CPacketServerQuery)

        } catch {
            case throwable: Throwable =>
                log.error(throwable)
                throwable.printStackTrace()
        }
    }
}
