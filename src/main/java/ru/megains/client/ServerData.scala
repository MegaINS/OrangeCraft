package ru.megains.client

class ServerData(val serverName: String, val serverIP: String, lanServer: Boolean) {


    var populationInfo: String = _
    /**
      * (better variable name would be 'hostname') server name as displayed in the server browser's second line (grey
      * text)
      */
    var serverMOTD: String = _
    /** last server ping that showed up in the server browser */
    var pingToServer: Long = 0L
    var version: Int = 210
    /** Game version for this server. */
    var gameVersion: String = "1.10.2"
    var pinged: Boolean = false
    var playerList: String = _
    // private var resourceMode: ServerData.ServerResourceMode = ServerData.ServerResourceMode.PROMPT
    private var serverIcon: String = _
    /** True if the server is a LAN server */


    /**
      * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
      */
    //    def getNBTCompound: NBTTagCompound = {
    //        val nbttagcompound: NBTTagCompound = new NBTTagCompound
    //        nbttagcompound.setString("name", this.serverName)
    //        nbttagcompound.setString("ip", this.serverIP)
    //        if (this.serverIcon != null) nbttagcompound.setString("icon", this.serverIcon)
    //        if (this.resourceMode eq ServerData.ServerResourceMode.ENABLED) nbttagcompound.setBoolean("acceptTextures", true)
    //        else if (this.resourceMode eq ServerData.ServerResourceMode.DISABLED) nbttagcompound.setBoolean("acceptTextures", false)
    //        nbttagcompound
    //    }

    //
    //    def getResourceMode: ServerData.ServerResourceMode = this.resourceMode
    //
    //    def setResourceMode(mode: ServerData.ServerResourceMode) {
    //        this.resourceMode = mode
    //    }

    /**
      * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData instance.
      */
    //    def getServerDataFromNBTCompound(nbtCompound: NBTTagCompound): ServerData = {
    //        val serverdata: ServerData = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false)
    //        if (nbtCompound.hasKey("icon", 8)) serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"))
    //        if (nbtCompound.hasKey("acceptTextures", 1)) if (nbtCompound.getBoolean("acceptTextures")) serverdata.setResourceMode(ServerData.ServerResourceMode.ENABLED)
    //        else serverdata.setResourceMode(ServerData.ServerResourceMode.DISABLED)
    //        else serverdata.setResourceMode(ServerData.ServerResourceMode.PROMPT)
    //        serverdata
    //    }

    /**
      * Returns the base-64 encoded representation of the server's icon, or null if not available
      */
    def getBase64EncodedIconData: String = this.serverIcon

    def setBase64EncodedIconData(icon: String) {
        this.serverIcon = icon
    }

    /**
      * Return true if the server is a LAN server
      */
    def isOnLAN: Boolean = this.lanServer

    //    def copyFrom(serverDataIn: ServerData) {
    //        this.serverIP = serverDataIn.serverIP
    //        this.serverName = serverDataIn.serverName
    //      //  this.setResourceMode(serverDataIn.getResourceMode)
    //        this.serverIcon = serverDataIn.serverIcon
    //        this.lanServer = serverDataIn.lanServer
    //    }

    //   final class ServerResourceMode private(val name: String) {
    //        this.motd = new TextComponentTranslation("addServer.resourcePack." + name, new Array[AnyRef](0))
    //        final private var motd: ITextComponent = null
    //
    //        def getMotd: ITextComponent = {
    //            return this.motd
    //        }
    //    }
}
