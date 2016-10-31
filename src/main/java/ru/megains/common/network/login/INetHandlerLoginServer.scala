package ru.megains.common.network.login

import ru.megains.common.network.INetHandler
import ru.megains.common.network.login.client.CPacketLoginStart

trait INetHandlerLoginServer extends INetHandler {

    def processLoginStart(packetIn: CPacketLoginStart)

    //  def processEncryptionResponse(packetIn: CPacketEncryptionResponse)
}
