package ru.megains.common.register

import ru.megains.client.OrangeCraft
import ru.megains.common.block.Block
import ru.megains.common.item.Item
import ru.megains.common.multiblock.MultiBlockSingle
import ru.megains.common.utils.Logger

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
