package ru.megains.common.network

import java.io.IOException
import java.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import ru.megains.common.network.PacketDirection.PacketDirection
import ru.megains.utils.Logger

class PacketDecoder(direction: PacketDirection) extends ByteToMessageDecoder with Logger[PacketDecoder] {

    override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {

        val size = in.readableBytes()
        if (size != 0) {
            val buffer = new PacketBuffer(in)
            val id = buffer.readInt()

            val packet = ctx.pipeline().channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get().getPacket(direction, id)

            if (packet == null) throw new IOException("Bad packet id " + id)
            val name = ConnectionState.getFromPacket(packet).name


            val packetName = packet.getClass.getSimpleName

            log.info(s"Decoder $name, packet $packetName, id $id, size $size")
            packet.readPacketData(buffer)

            if (in.readableBytes > 0) throw new IOException("Packet was larger than I expected, found " + in.readableBytes + " bytes extra whilst reading packet " + id)
            else out.add(packet)

        }

    }
}
