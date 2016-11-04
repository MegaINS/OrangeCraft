package ru.megains.common.block.blockdata

sealed abstract class BlockDirection private(val x: Int, val y: Int, val z: Int, val name: String, val id: Int) {

}

object BlockDirection {


    case object DOWN extends BlockDirection(0, -1, 0, "down", 0)

    case object UP extends BlockDirection(0, 1, 0, "up", 1)

    case object NORTH extends BlockDirection(0, 0, -1, "north", 2)

    case object SOUTH extends BlockDirection(0, 0, 1, "south", 3)

    case object WEST extends BlockDirection(-1, 0, 0, "west", 4)

    case object EAST extends BlockDirection(1, 0, 0, "east", 5)

    val DIRECTIONAL_BY_ID = Array(DOWN, UP, NORTH, SOUTH, WEST, EAST)

}
