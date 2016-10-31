package ru.megains.utils

import ru.megains.common.network.{INetHandler, Packet}


object PacketThreadUtil {

    def checkThreadAndEnqueue[T <: INetHandler](packetIn: Packet[T], processor: T, scheduler: IThreadListener) {

        if (!scheduler.isCallingFromMinecraftThread) {
            scheduler.addScheduledTask(new Runnable() {
                def run() {
                    packetIn.processPacket(processor)
                }
            })
            throw ThreadQuickExitException.INSTANCE
        }
    }
}