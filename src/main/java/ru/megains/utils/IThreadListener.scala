package ru.megains.utils

import com.google.common.util.concurrent.ListenableFuture

trait IThreadListener {
    def addScheduledTask(runnableToSchedule: Runnable): ListenableFuture[AnyRef]

    def isCallingFromMinecraftThread: Boolean
}
