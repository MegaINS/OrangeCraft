package ru.megains.server

import scala.collection.mutable
import scala.io.StdIn

class ServerCommand(server: OrangeCraftServer) extends Thread {

    val commands: mutable.HashMap[String, () => Unit] = new mutable.HashMap[String, () => Unit]

    initCommand()


    override def run(): Unit = {

        while (server.serverRunning) {
            val command = StdIn.readLine()
            parseCommand(command.trim)
        }

    }

    def parseCommand(command: String): Unit = {
        commands.getOrElse(command, () => println(s"Command [$command] not found"))()
    }

    def initCommand() = {
        commands += "" -> (() => {})
        commands += "stop" -> (() => server.serverRunning = false)
        commands += "help" -> (() => {
            println("All commands:")
            commands.keySet.foreach(println)
        })
        commands += "players" -> (() => server.playerList.nameToPlayerMap.keySet.foreach(println))
    }


}
