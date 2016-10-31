package ru.megains.common.world

sealed abstract class GameType private(val id: Int, val name: String, val shortName: String) {


    //    def configurePlayerCapabilities(capabilities: PlayerCapabilities) {
    //        if (this eq GameType.CREATIVE) {
    //            capabilities.allowFlying = true
    //            capabilities.isCreativeMode = true
    //            capabilities.disableDamage = true
    //        }
    //        else if (this eq GameType.SPECTATOR) {
    //            capabilities.allowFlying = true
    //            capabilities.isCreativeMode = false
    //            capabilities.disableDamage = true
    //            capabilities.isFlying = true
    //        }
    //        else {
    //            capabilities.allowFlying = false
    //            capabilities.isCreativeMode = false
    //            capabilities.disableDamage = false
    //            capabilities.isFlying = false
    //        }
    //        capabilities.allowEdit = !this.isAdventure
    //    }


    def isAdventure: Boolean = (this eq GameType.ADVENTURE) || (this eq GameType.SPECTATOR)

    def isCreative: Boolean = this eq GameType.CREATIVE

    def isSurvivalOrAdventure: Boolean = (this eq GameType.SURVIVAL) || (this eq GameType.ADVENTURE)
}


object GameType {


    case object NOT_SET extends GameType(-1, "", "")

    case object SURVIVAL extends GameType(0, "survival", "s")

    case object CREATIVE extends GameType(1, "creative", "c")

    case object ADVENTURE extends GameType(2, "adventure", "a")

    case object SPECTATOR extends GameType(3, "spectator", "sp")


}
