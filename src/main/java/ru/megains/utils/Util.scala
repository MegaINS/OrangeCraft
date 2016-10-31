package ru.megains.utils

import java.util.concurrent.{ExecutionException, FutureTask}

import ru.megains.utils.Util.EnumOS.EnumOS

object Util {
    def getOSType: EnumOS = {
        val s: String = System.getProperty("os.name").toLowerCase
        if (s.contains("win")) Util.EnumOS.WINDOWS
        else if (s.contains("mac")) Util.EnumOS.OSX
        else if (s.contains("solaris")) Util.EnumOS.SOLARIS
        else if (s.contains("solaris")) Util.EnumOS.SOLARIS
        else if (s.contains("sunos")) Util.EnumOS.SOLARIS
        else if (s.contains("sunos")) Util.EnumOS.SOLARIS
        else if (s.contains("linux")) Util.EnumOS.LINUX
        else if (s.contains("linux")) Util.EnumOS.LINUX
        else if (s.contains("unix")) Util.EnumOS.LINUX
        else Util.EnumOS.UNKNOWN
    }

    def runTask[V](task: FutureTask[V], logger: org.apache.logging.log4j.Logger): V = {
        try {
            task.run()
            task.get
        } catch {
            case executionexception: ExecutionException =>
                logger.fatal("Error executing task", executionexception.asInstanceOf[Throwable])
                null.asInstanceOf

            case interruptedexception: InterruptedException =>
                logger.fatal("Error executing task", interruptedexception.asInstanceOf[Throwable])
                null.asInstanceOf


        }

    }

    // def getLastElement[T](list: util.List[T]): T = list.get(list.size - 1).asInstanceOf[T]

    object EnumOS extends Enumeration {
        type EnumOS = Value
        val LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN = Value
    }

}