package ru.megains.common.network

import java.util.concurrent.Semaphore


class ServerStatusResponse {
    private var description: String = null
    //  private var players: ServerStatusResponse.Players = null
    private var version: ServerStatusResponse.Version = null
    private var favicon: String = null

    def getServerDescription: String = {
        description
    }

    def setServerDescription(descriptionIn: String) {
        this.description = descriptionIn
        invalidateJson()
    }

    //    def getPlayers: ServerStatusResponse.Players = {
    //        return this.players
    //    }
    //
    //    def setPlayers(playersIn: ServerStatusResponse.Players) {
    //        this.players = playersIn
    //        invalidateJson()
    //    }

    def getVersion: ServerStatusResponse.Version = {
        return this.version
    }

    def setVersion(versionIn: ServerStatusResponse.Version) {
        this.version = versionIn
        invalidateJson()
    }

    def setFavicon(faviconBlob: String) {
        this.favicon = faviconBlob
        invalidateJson()
    }

    def getFavicon: String = {
        return this.favicon
    }

    private val mutex: Semaphore = new Semaphore(1)
    private var json: String = null

    /**
      * Returns this object as a Json string.
      * Converting to JSON if a cached version is not available.
      *
      * Also to prevent potentially large memory allocations on the server
      * this is moved from the S00PacketServerInfo writePacket function
      *
      * As this method is called from the network threads thread safety is important!
      *
      * @return
      */
    //    def getJson: String = {
    //        var ret: String = this.json
    //        if (ret == null) {
    //            mutex.acquireUninterruptibly()
    //            ret = this.json
    //            if (ret == null) {
    //                ret = net.minecraft.network.status.server.SPacketServerInfo.GSON.toJson(this)
    //                this.json = ret
    //            }
    //            mutex.release()
    //        }
    //        return ret
    //    }

    /**
      * Invalidates the cached json, causing the next call to getJson to rebuild it.
      * This is needed externally because PlayerCountData.setPlayer's is public.
      */
    def invalidateJson() {
        this.json = null
    }
}

object ServerStatusResponse {

    //    object Players {
    //
    //        class Serializer extends JsonDeserializer[ServerStatusResponse.Players] with JsonSerializer[ServerStatusResponse.Players] {
    //            @throws[JsonParseException]
    //            def deserialize(p_deserialize_1_:
    //
    //            JsonElement, p_deserialize_2_: Type, p_deserialize_3_: JsonDeserializationContext): ServerStatusResponse.Players = {
    //                val jsonobject: JsonObject = JsonUtils.getJsonObject(p_deserialize_1_, "players")
    //                val serverstatusresponse$players: ServerStatusResponse.Players = new ServerStatusResponse.Players(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"))
    //                if (JsonUtils.isJsonArray(jsonobject, "sample")) {
    //                    val jsonarray: JsonArray = JsonUtils.getJsonArray(jsonobject, "sample")
    //                    if (jsonarray.size > 0) {
    //                        val agameprofile: Array[GameProfile] = new Array[GameProfile](jsonarray.size)
    //                        var i: Int = 0
    //                        while (i < agameprofile.length) {
    //                            {
    //                                val jsonobject1: JsonObject = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]")
    //                                val s: String = JsonUtils.getString(jsonobject1, "id")
    //                                agameprofile(i) = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject1, "name"))
    //                            }
    //                            {
    //                                i += 1; i
    //                            }
    //                        }
    //                        serverstatusresponse$players.setPlayers(agameprofile)
    //                    }
    //                }
    //                return serverstatusresponse$players
    //            }
    //
    //            def serialize(p_serialize_1_:
    //
    //            ServerStatusResponse.Players, p_serialize_2_: Type, p_serialize_3_: JsonSerializationContext): JsonElement = {
    //                val jsonobject: JsonObject = new JsonObject
    //                jsonobject.addProperty("max", Integer.valueOf(p_serialize_1_.getMaxPlayers).asInstanceOf[Number])
    //                jsonobject.addProperty("online", Integer.valueOf(p_serialize_1_.getOnlinePlayerCount).asInstanceOf[Number])
    //                if (p_serialize_1_.getPlayers != null && p_serialize_1_.getPlayers.length > 0) {
    //                    val jsonarray: JsonArray = new JsonArray
    //                    var i: Int = 0
    //                    while (i < p_serialize_1_.getPlayers.length) {
    //                        {
    //                            val jsonobject1: JsonObject = new JsonObject
    //                            val uuid: UUID = p_serialize_1_.getPlayers(i).getId
    //                            jsonobject1.addProperty("id", if (uuid == null) ""
    //                            else uuid.toString)
    //                            jsonobject1.addProperty("name", p_serialize_1_.getPlayers(i).getName)
    //                            jsonarray.add(jsonobject1)
    //                        }
    //                        {
    //                            i += 1; i
    //                        }
    //                    }
    //                    jsonobject.add("sample", jsonarray)
    //                }
    //                return jsonobject
    //            }
    //        }
    //
    //    }

    //    class Players(val maxPlayers: Int, val onlinePlayerCount: Int) {
    //        private var players: Array[GameProfile] = null
    //
    //        def getMaxPlayers: Int = this.maxPlayers
    //
    //        def getOnlinePlayerCount: Int = this.onlinePlayerCount
    //
    //        def getPlayers: Array[GameProfile] = this.players
    //
    //        def setPlayers(playersIn: Array[GameProfile]) {
    //            this.players = playersIn
    //        }
    //    }

    //    class Serializer extends JsonDeserializer[ServerStatusResponse] with JsonSerializer[ServerStatusResponse] {
    //        @throws[JsonParseException]
    //        def deserialize(p_deserialize_1_:
    //
    //        JsonElement, p_deserialize_2_: Type, p_deserialize_3_: JsonDeserializationContext): ServerStatusResponse = {
    //            val jsonobject: JsonObject = JsonUtils.getJsonObject(p_deserialize_1_, "status")
    //            val serverstatusresponse: ServerStatusResponse = new ServerStatusResponse
    //            if (jsonobject.has("description")) serverstatusresponse.setServerDescription(p_deserialize_3_.deserialize(jsonobject.get("description"), classOf[ITextComponent]).asInstanceOf[ITextComponent])
    //            if (jsonobject.has("players")) serverstatusresponse.setPlayers(p_deserialize_3_.deserialize(jsonobject.get("players"), classOf[ServerStatusResponse.Players]).asInstanceOf[ServerStatusResponse.Players])
    //            if (jsonobject.has("version")) serverstatusresponse.setVersion(p_deserialize_3_.deserialize(jsonobject.get("version"), classOf[ServerStatusResponse.Version]).asInstanceOf[ServerStatusResponse.Version])
    //            if (jsonobject.has("favicon")) serverstatusresponse.setFavicon(JsonUtils.getString(jsonobject, "favicon"))
    //            net.minecraftforge.fml.client.FMLClientHandler.instance.captureAdditionalData(serverstatusresponse, jsonobject)
    //            return serverstatusresponse
    //        }
    //
    //        def serialize(p_serialize_1_:
    //
    //        ServerStatusResponse, p_serialize_2_: Type, p_serialize_3_: JsonSerializationContext): JsonElement = {
    //            val jsonobject: JsonObject = new JsonObject
    //            if (p_serialize_1_.getServerDescription != null) jsonobject.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerDescription))
    //            if (p_serialize_1_.getPlayers != null) jsonobject.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayers))
    //            if (p_serialize_1_.getVersion != null) jsonobject.add("version", p_serialize_3_.serialize(p_serialize_1_.getVersion))
    //            if (p_serialize_1_.getFavicon != null) jsonobject.addProperty("favicon", p_serialize_1_.getFavicon)
    //            net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.enhanceStatusQuery(jsonobject)
    //            return jsonobject
    //        }
    //    }

    //    object Version {
    //
    //        class Serializer extends JsonDeserializer[ServerStatusResponse.Version] with JsonSerializer[ServerStatusResponse.Version] {
    //            @throws[JsonParseException]
    //            def deserialize(p_deserialize_1_:
    //
    //            JsonElement, p_deserialize_2_: Type, p_deserialize_3_: JsonDeserializationContext): ServerStatusResponse.Version = {
    //                val jsonobject: JsonObject = JsonUtils.getJsonObject(p_deserialize_1_, "version")
    //                return new ServerStatusResponse.Version(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"))
    //            }
    //
    //            def serialize(p_serialize_1_:
    //
    //            ServerStatusResponse.Version, p_serialize_2_: Type, p_serialize_3_: JsonSerializationContext): JsonElement = {
    //                val jsonobject: JsonObject = new JsonObject
    //                jsonobject.addProperty("name", p_serialize_1_.getName)
    //                jsonobject.addProperty("protocol", Integer.valueOf(p_serialize_1_.getProtocol).asInstanceOf[Number])
    //                return jsonobject
    //            }
    //        }
    //
    //    }

    class Version(val name: String, val protocol: Int) {
        def getName: String = this.name

        def getProtocol: Int = this.protocol
    }

}
