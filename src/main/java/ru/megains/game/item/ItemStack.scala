package ru.megains.game.item

import ru.megains.game.block.Block

class ItemStack(val item: Item, var stackSize: Int) {

    def this(block: Block, stackSize: Int) = this(Item.getItemFromBlock(block), stackSize)

    def this(block: Block) = this(block, 1)

    def this(item: Item) = this(item, 1)


}
