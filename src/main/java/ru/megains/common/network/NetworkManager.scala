package ru.megains.common.network

import java.net.{InetAddress, SocketAddress}
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantReadWriteLock

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.local.LocalChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.AttributeKey
import ru.megains.common.ITickable
import ru.megains.common.network.PacketDirection.PacketDirection
import ru.megains.utils.{Logger, ThreadQuickExitException}


class NetworkManager(direction: PacketDirection) extends SimpleChannelInboundHandler[Packet[INetHandler]] with Logger[NetworkManager] {


    var channel: Channel = _
    var packetListener: INetHandler = _
    var disconnected = false

    private val readWriteLock: ReentrantReadWriteLock = new ReentrantReadWriteLock
    private val outboundPacketsQueue: ConcurrentLinkedQueue[Packet[_ <: INetHandler]] = new ConcurrentLinkedQueue[Packet[_ <: INetHandler]]


    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        super.channelActive(ctx)
        channel = ctx.channel()
        setConnectionState(ConnectionState.HANDSHAKING)
    }

    override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
        log.info(cause)
        closeChannel("exceptionCaught")
    }

    override def channelInactive(ctx: ChannelHandlerContext): Unit = {
        closeChannel("channelInactive")
    }


    def setConnectionState(connectionState: ConnectionState) {

        channel.attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).set(connectionState)
        channel.config.setAutoRead(true)

    }

    def setNetHandler(handler: INetHandler) {
        log.debug("Set listener of {} to {}", Array[AnyRef](this, handler))
        packetListener = handler
    }


    def sendPacket(packetIn: Packet[_ <: INetHandler]) {
        if (isChannelOpen) {
            flushOutboundQueue()
            dispatchPacket(packetIn)
        } else {
            readWriteLock.writeLock.lock()
            try {
                outboundPacketsQueue.add(packetIn)
            } finally readWriteLock.writeLock.unlock()
        }

    }

    def isChannelOpen: Boolean = channel != null && channel.isOpen

    private def flushOutboundQueue() {
        if (isChannelOpen) {
            readWriteLock.readLock.lock()
            try {
                while (!outboundPacketsQueue.isEmpty) {
                    val packet: Packet[_] = outboundPacketsQueue.poll
                    dispatchPacket(packet)
                }
            }
            finally readWriteLock.readLock.unlock()
        }
    }

    private def dispatchPacket(inPacket: Packet[_]) {
        val enumconnectionstate: ConnectionState = ConnectionState.getFromPacket(inPacket)
        val enumconnectionstate1: ConnectionState = channel.attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get
        if (enumconnectionstate1 ne enumconnectionstate) {
            log.debug("Disabled auto read")
            channel.config.setAutoRead(false)
        }
        if (channel.eventLoop.inEventLoop) {
            if (enumconnectionstate ne enumconnectionstate1) this.setConnectionState(enumconnectionstate)
            val channelfuture: ChannelFuture = this.channel.writeAndFlush(inPacket)
            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
        }
        else {

            channel.eventLoop.execute(new Runnable() {
                def run() {
                    if (enumconnectionstate ne enumconnectionstate1) setConnectionState(enumconnectionstate)
                    val channelfuture1: ChannelFuture = channel.writeAndFlush(inPacket)

                    channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
                }
            })
        }

    }

    def closeChannel(error: String): Unit = {
        if (channel.isOpen) {
            channel.close().awaitUninterruptibly
            println(error)
        }
    }


    override def messageReceived(ctx: ChannelHandlerContext, packet: Packet[INetHandler]): Unit = {

        if (channel.isOpen) try {
            packet.processPacket(packetListener)
        } catch {
            case var4: ThreadQuickExitException =>

        }

    }

    def processReceivedPackets() {
        flushOutboundQueue()





        packetListener match {
            case tickable: ITickable => tickable.update()
            case _ =>
        }



        channel.flush
    }

    def hasNoChannel: Boolean = channel == null


    def checkDisconnected() {
        if (channel != null && !channel.isOpen) if (disconnected) log.warn("handleDisconnection() called twice")
        else {
            disconnected = true
            //  if (getExitMessage != null) getNetHandler.onDisconnect(getExitMessage)
            // else
            if (packetListener != null) packetListener.onDisconnect("Disconnected")
        }
    }

}

object NetworkManager {


    val PROTOCOL_ATTRIBUTE_KEY: AttributeKey[ConnectionState] = AttributeKey.newInstance[ConnectionState]("protocol")

    def createLanClient(): NetworkManager = {
        val networkManager = new NetworkManager(PacketDirection.CLIENTBOUND)
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer[NioSocketChannel] {
                    override def initChannel(ch: NioSocketChannel): Unit = {
                        ch.pipeline()
                                .addLast("splitter", new NettyVarint21FrameDecoder)
                                .addLast("decoder", new PacketDecoder(PacketDirection.CLIENTBOUND))
                                .addLast("prepender", new NettyVarint21FrameEncoder)
                                .addLast("encoder", new PacketEncoder(PacketDirection.SERVERBOUND))
                                .addLast("packetHandler", networkManager)

                    }
                })
                .channel(classOf[NioSocketChannel])
                .connect("localhost", 8080).syncUninterruptibly()
        networkManager
    }

    def createLocalClient(socket: SocketAddress): NetworkManager = {
        val networkManager = new NetworkManager(PacketDirection.CLIENTBOUND)
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer[LocalChannel] {
                    override def initChannel(ch: LocalChannel): Unit = {
                        ch.pipeline()
                                .addLast("packetHandler", networkManager)
                    }
                })
                .channel(classOf[LocalChannel])
                .connect(socket).syncUninterruptibly()
        networkManager
    }

    def createNetworkManagerAndConnect(address: InetAddress, serverPort: Int): NetworkManager = {
        val networkManager: NetworkManager = new NetworkManager(PacketDirection.CLIENTBOUND)
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer[NioSocketChannel] {
                    override def initChannel(ch: NioSocketChannel): Unit = {
                        ch.pipeline()
                                .addLast("splitter", new NettyVarint21FrameDecoder)
                                .addLast("decoder", new PacketDecoder(PacketDirection.CLIENTBOUND))
                                .addLast("prepender", new NettyVarint21FrameEncoder)
                                .addLast("encoder", new PacketEncoder(PacketDirection.SERVERBOUND))
                                .addLast("packetHandler", networkManager)

                    }
                })
                .channel(classOf[NioSocketChannel])
                .connect(address, serverPort)
                .syncUninterruptibly
        networkManager
    }


}
