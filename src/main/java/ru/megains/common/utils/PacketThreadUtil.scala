package ru.megains.common.utils

import ru.megains.common.network.{INetHandler, Packet}


object PacketThreadUtil {

    def checkThreadAndEnqueue[T <: INetHandler](packetIn: Packet[T], processor: T, scheduler: IThreadListener) {

        if (!scheduler.isCallingFromMinecraftThread) {
            scheduler.addScheduledTask(() => {
                packetIn.processPacket(processor)
            })
            throw ThreadQuickExitException.INSTANCE
        }
    }
}