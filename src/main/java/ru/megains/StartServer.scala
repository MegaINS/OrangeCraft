package ru.megains

import ru.megains.server.{OrangeCraftServer, ServerCommand}

import scala.reflect.io.Path

object StartServer extends App {

    Thread.currentThread.setName("Server thread")

    val server = new OrangeCraftServer(Path("Z:/OrangeCraft/server").toDirectory)
    val serverCommand = new ServerCommand(server)
    serverCommand.setName("serverControl")
    serverCommand.setDaemon(true)
    serverCommand.start()

    server.run()


}
