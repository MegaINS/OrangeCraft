package ru.megains.client.network

import ru.megains.client.renderer.gui.GuiScreen
import ru.megains.common.network.login.INetHandlerLoginClient
import ru.megains.common.network.login.server.{SPacketDisconnect, SPacketLoginSuccess}
import ru.megains.common.network.{ConnectionState, NetHandlerPlayClient, NetworkManager}
import ru.megains.game.OrangeCraft
import ru.megains.utils.PacketThreadUtil

class NetHandlerLoginClient(networkManager: NetworkManager, gameController: OrangeCraft, previousScreen: GuiScreen) extends INetHandlerLoginClient {


    override def handleDisconnect(packetIn: SPacketDisconnect): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)
    }

    override def onDisconnect(msg: String): Unit = {

    }

    override def handleLoginSuccess(packetIn: SPacketLoginSuccess): Unit = {
        networkManager.setConnectionState(ConnectionState.PLAY)
        val nhpc: NetHandlerPlayClient = new NetHandlerPlayClient(gameController, previousScreen, networkManager)
        networkManager.setNetHandler(nhpc)
    }
}
