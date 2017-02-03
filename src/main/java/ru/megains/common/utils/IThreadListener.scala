package ru.megains.common.utils



trait IThreadListener {

    def addScheduledTask(runnableToSchedule: () => Unit): Unit

    def isCallingFromMinecraftThread: Boolean
}
