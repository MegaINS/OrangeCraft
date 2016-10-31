package ru.megains.server.network.handshake

import ru.megains.common.network.INetHandler
import ru.megains.common.network.handshake.client.CHandshake

trait INetHandlerHandshakeServer extends INetHandler {


    def processHandshake(packet: CHandshake)
}
