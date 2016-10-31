package ru.megains.common.network.status

import ru.megains.common.network.INetHandler
import ru.megains.common.network.status.client.{CPacketPing, CPacketServerQuery}

trait INetHandlerStatusServer extends INetHandler {
    def processPing(packetIn: CPacketPing)

    def processServerQuery(packetIn: CPacketServerQuery)
}
