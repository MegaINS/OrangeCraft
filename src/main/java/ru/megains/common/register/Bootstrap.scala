package ru.megains.common.register

import ru.megains.client.OrangeCraft
import ru.megains.common.item.Item
import ru.megains.common.utils.Logger

object Bootstrap extends Logger[OrangeCraft] {

    var isNotInit = true

    def init(): Unit = {
        if (isNotInit) {
            isNotInit = false
            log.info("Blocks init...")
            Blocks.initBlocks()
            log.info("Items init...")
            Item.initItems()
        }

    }


}
