package ru.megains.common.utils

object ThreadQuickExitException {
    val INSTANCE: ThreadQuickExitException = new ThreadQuickExitException
}

final class ThreadQuickExitException private() extends RuntimeException {
    this.setStackTrace(new Array[StackTraceElement](0))

    override def fillInStackTrace: Throwable = {
        this.setStackTrace(new Array[StackTraceElement](0))
        this
    }
}
