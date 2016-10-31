package ru.megains.common.network.status.server

import java.io.IOException

import ru.megains.common.network.status.INetHandlerStatusClient
import ru.megains.common.network.{Packet, PacketBuffer, ServerStatusResponse}


class SPacketServerInfo() extends Packet[INetHandlerStatusClient] {
    private var response: ServerStatusResponse = null

    def this(responseIn: ServerStatusResponse) {
        this()
        this.response = responseIn
    }

    /**
      * Reads the raw packet data from the data stream.
      */
    @throws[IOException]
    def readPacketData(buf: PacketBuffer) {
        //  this.response = JsonUtils.gsonDeserialize(SPacketServerInfo.GSON, buf.readStringFromBuffer(32767), classOf[ServerStatusResponse]).asInstanceOf[ServerStatusResponse]
    }

    /**
      * Writes the raw packet data to the data stream.
      */
    @throws[IOException]
    def writePacketData(buf: PacketBuffer) {
        //  buf.writeString(SPacketServerInfo.GSON.toJson(this.response.asInstanceOf[Any]))
    }

    /**
      * Passes this Packet on to the NetHandler for processing.
      */
    def processPacket(handler: INetHandlerStatusClient) {
        handler.handleServerInfo(this)
    }

    // def getResponse: ServerStatusResponse = this.response
}

object SPacketServerInfo {
    //  val GSON: Gson = (new GsonBuilder).registerTypeAdapter(classOf[ServerStatusResponse.Version], new ServerStatusResponse.Version#Serializer).registerTypeAdapter(classOf[ServerStatusResponse.Players], new ServerStatusResponse.Players#Serializer).registerTypeAdapter(classOf[ServerStatusResponse], new ServerStatusResponse.Serializer).registerTypeHierarchyAdapter(classOf[ITextComponent], new ITextComponent.Serializer).registerTypeHierarchyAdapter(classOf[Style], new Style.Serializer).registerTypeAdapterFactory(new EnumTypeAdapterFactory).create
}