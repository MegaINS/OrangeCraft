package ru.megains.common.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder


class NettyVarint21FrameEncoder extends MessageToByteEncoder[ByteBuf] {


    override def encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf): Unit = {
        val i: Int = msg.readableBytes
        val j: Int = PacketBuffer.getVarIntSize(i)
        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3)
        } else {
            val packetbuffer: PacketBuffer = new PacketBuffer(out)
            packetbuffer.ensureWritable(j + i)
            packetbuffer.writeVarIntToBuffer(i)
            packetbuffer.writeBytes(msg, msg.readerIndex, i)
        }
    }
}
