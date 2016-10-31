package ru.megains.server.network

import java.net.InetAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.local.{LocalAddress, LocalChannel, LocalServerChannel}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.{NioServerSocketChannel, NioSocketChannel}
import io.netty.channel.{ChannelFuture, ChannelInitializer, ChannelOption, EventLoopGroup}
import ru.megains.common.network._
import ru.megains.server.OrangeCraftServer

import scala.collection.mutable.ArrayBuffer

class NetworkSystem(server: OrangeCraftServer) {

    var networkServer: ServerBootstrap = _
    var channelFuture: ChannelFuture = _


    def startLan(address: InetAddress, port: Int): Unit = {
        val bossExec: EventLoopGroup = new NioEventLoopGroup(0)

        networkServer = new ServerBootstrap()
                .group(bossExec)
                .localAddress(address, port)
                .channel(classOf[NioServerSocketChannel])
                .childHandler(new ChannelInitializer[NioSocketChannel] {
                    override def initChannel(ch: NioSocketChannel): Unit = {
                        val networkManager = new NetworkManager(PacketDirection.SERVERBOUND)
                        ch.pipeline()
                                .addLast("splitter", new NettyVarint21FrameDecoder)
                                .addLast("decoder", new PacketDecoder(PacketDirection.SERVERBOUND))
                                .addLast("prepender", new NettyVarint21FrameEncoder)
                                .addLast("encoder", new PacketEncoder(PacketDirection.CLIENTBOUND))
                                .addLast("packetHandler", networkManager)
                        ch.config.setOption(ChannelOption.TCP_NODELAY, Boolean.box(true))


                        NetworkSystem.networkManagers :+= networkManager

                        networkManager.setNetHandler(new NetHandlerHandshakeTCP(server, networkManager))
                    }
                })
        channelFuture = networkServer.bind.syncUninterruptibly()
        //channelFuture.channel().closeFuture().sync()

    }

    def startLocal(): Unit = {

        networkServer = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .localAddress(LocalAddress.ANY)
                .channel(classOf[LocalServerChannel])
                .childHandler(new ChannelInitializer[LocalChannel] {
                    override def initChannel(ch: LocalChannel): Unit = {
                        val networkManager = new NetworkManager(PacketDirection.SERVERBOUND)
                        ch.pipeline().addLast("packetHandler", networkManager)
                        NetworkSystem.networkManagers :+= networkManager
                    }
                })

        channelFuture = networkServer.bind.syncUninterruptibly()
        //  channelFuture.channel().closeFuture().sync()


    }

    def networkTick() {

        NetworkSystem.networkManagers = NetworkSystem.networkManagers.flatMap(
            (networkManager) => {

                if (!networkManager.hasNoChannel) {
                    if (networkManager.isChannelOpen) {

                        try {
                            networkManager.processReceivedPackets()
                        } catch {
                            case exception: Exception =>
                                exception.printStackTrace()
                        }
                        Some(networkManager)
                    } else {
                        networkManager.checkDisconnected()
                        None
                    }
                } else {
                    Some(networkManager)
                }

            }

        )

    }


}

object NetworkSystem {
    var networkManagers: ArrayBuffer[NetworkManager] = new ArrayBuffer[NetworkManager]
}
