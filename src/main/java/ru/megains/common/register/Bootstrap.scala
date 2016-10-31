package ru.megains.common.register

import ru.megains.game.OrangeCraft
import ru.megains.game.block.Block
import ru.megains.game.item.Item
import ru.megains.game.multiblock.MultiBlockSingle
import ru.megains.utils.Logger

object Bootstrap extends Logger[OrangeCraft] {

    var isNotInit = true

    def init() = {
        if (isNotInit) {
            isNotInit = false
            log.info("Blocks init...")
            Block.initBlocks()
            log.info("Items init...")
            Item.initItems()
            log.info("MultiBlockSingle init...")
            MultiBlockSingle.initMultiBlockSingle()
        }

    }


}
