package ru.megains.common.block.blockdata

import ru.megains.common.block.blockdata.BlockFormat.STANDART

sealed abstract class BlockFormat extends Enumeration {

    def isStandart: Boolean = this == STANDART

}

object BlockFormat {

    case object MICRO extends BlockFormat

    case object STANDART extends BlockFormat

    case object MACRO extends BlockFormat

    case object MULTI extends BlockFormat

}