package ru.megains.common.network.status

import ru.megains.common.network.INetHandler
import ru.megains.common.network.status.server.{SPacketPong, SPacketServerInfo}

trait INetHandlerStatusClient extends INetHandler {


    def handleServerInfo(packetIn: SPacketServerInfo)

    def handlePong(packetIn: SPacketPong)
}