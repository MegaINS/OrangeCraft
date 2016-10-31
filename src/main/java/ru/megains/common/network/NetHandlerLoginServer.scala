package ru.megains.common.network

import ru.megains.common.ITickable
import ru.megains.common.network.login.INetHandlerLoginServer
import ru.megains.common.network.login.client.CPacketLoginStart
import ru.megains.common.network.login.server.SPacketLoginSuccess
import ru.megains.server.OrangeCraftServer
import ru.megains.server.entity.EntityPlayerMP

class NetHandlerLoginServer(server: OrangeCraftServer, networkManager: NetworkManager) extends INetHandlerLoginServer with ITickable {

    var currentLoginState = LoginState.HELLO
    var name: String = _


    override def processLoginStart(packetIn: CPacketLoginStart): Unit = {
        currentLoginState = LoginState.READY_TO_ACCEPT
        name = packetIn.name
    }


    override def onDisconnect(msg: String): Unit = {

    }

    def update() {
        if (currentLoginState eq LoginState.READY_TO_ACCEPT) tryAcceptPlayer()

        //        if ( {
        //            connectionTimer += 1; connectionTimer - 1
        //        } == 600) closeConnection("Took too long to log in")
    }

    def tryAcceptPlayer() {
        //  val s: String = server.playerList.allowUserToConnect(networkManager.getRemoteAddress, loginGameProfile)
        //  if (s != null) closeConnection(s)
        //   else {
        currentLoginState = LoginState.ACCEPTED
        networkManager.sendPacket(new SPacketLoginSuccess())

        var entityPlayer: EntityPlayerMP = server.playerList.getPlayerByName(name)
        if (entityPlayer == null) {
            entityPlayer = server.playerList.createPlayerForUser(name)
        }
        server.playerList.initializeConnectionToPlayer(networkManager, entityPlayer)
        // }
    }

    object LoginState extends Enumeration {
        type LoginState = Value
        val HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED = Value
    }


}


