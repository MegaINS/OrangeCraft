package ru.megains

import ru.megains.client.OrangeCraft

object StartClient extends App {

    Thread.currentThread.setName("Client")
    val oc: OrangeCraft = new OrangeCraft("C:/OrangeCraft/")
    oc.run()

}
