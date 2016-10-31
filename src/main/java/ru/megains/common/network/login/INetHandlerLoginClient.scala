package ru.megains.common.network.login

import ru.megains.common.network.INetHandler
import ru.megains.common.network.login.server.{SPacketDisconnect, SPacketLoginSuccess}

trait INetHandlerLoginClient extends INetHandler {
    // def handleEncryptionRequest(packetIn: SPacketEncryptionRequest)

    def handleLoginSuccess(packetIn: SPacketLoginSuccess)

    def handleDisconnect(packetIn: SPacketDisconnect)

    // def handleEnableCompression(packetIn: SPacketEnableCompression)
}
