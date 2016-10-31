package ru.megains.common.network

import java.util

import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.{ByteToMessageDecoder, CorruptedFrameException}

class NettyVarint21FrameDecoder extends ByteToMessageDecoder {


    override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {
        in.markReaderIndex
        val abyte: Array[Byte] = new Array[Byte](3)
        var i: Int = 0
        while (i < abyte.length) {
            {
                if (!in.isReadable) {
                    in.resetReaderIndex
                    return
                }
                abyte(i) = in.readByte
                if (abyte(i) >= 0) {
                    val packetbuffer: PacketBuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte))
                    try {
                        val j: Int = packetbuffer.readVarIntFromBuffer
                        if (in.readableBytes >= j) {
                            out.add(in.readBytes(j))
                            return
                        }
                        in.resetReaderIndex
                    }
                    finally packetbuffer.release
                    return
                }
            }
            {
                i += 1;
                i
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit")
    }
}
