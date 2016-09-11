package ru.megains.game.blockdata

class BlockDirection private(val x: Int, val y: Int, val z: Int, val name: String) {

}

object BlockDirection {


    val DOWN = BlockDirection(0, -1, 0, "down")
    val UP = BlockDirection(0, 1, 0, "up")
    val NORTH = BlockDirection(0, 0, -1, "north")
    val SOUTH = BlockDirection(0, 0, 1, "south")
    val WEST = BlockDirection(-1, 0, 0, "west")
    val EAST = BlockDirection(1, 0, 0, "east")

    def apply(x: Int, y: Int, z: Int, name: String) = new BlockDirection(x, y, z, name)


}
