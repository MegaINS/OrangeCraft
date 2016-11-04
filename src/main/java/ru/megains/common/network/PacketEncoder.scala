package ru.megains.common.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import ru.megains.common.network.PacketDirection.PacketDirection
import ru.megains.common.utils.Logger

class PacketEncoder(direction: PacketDirection) extends MessageToByteEncoder[Packet[_]] with Logger[PacketEncoder] {


    override def encode(ctx: ChannelHandlerContext, msg: Packet[_], out: ByteBuf): Unit = {


        val id = ctx.pipeline().channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get().getPacketId(direction, msg.getClass)
        val buffer = new PacketBuffer(out)

        buffer.writeInt(id)
        msg.writePacketData(buffer)
        val size = out.readableBytes()
        val name = ConnectionState.getFromPacket(msg).name
        val packetName = msg.getClass.getSimpleName
        log.info(s"Encoder $name, packet $packetName, id $id, size $size")
    }

}
