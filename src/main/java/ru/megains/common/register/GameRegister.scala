package ru.megains.common.register

import ru.megains.client.renderer.api.{ARenderBlock, ARenderItem}
import ru.megains.client.renderer.block.RenderBlockStandart
import ru.megains.client.renderer.item.{RenderItemBlock, RenderItemStandart}
import ru.megains.common.block.Block
import ru.megains.common.item.{Item, ItemBlock}
import ru.megains.common.multiblock.{AMultiBlock, MultiBlock, MultiBlockSingle}

object GameRegister {



    private val blockData = new RegisterNamespace[Block] with RegisterRender[ARenderBlock] {
        override val default = RenderBlockStandart

    }
    private val itemData = new RegisterNamespace[Item] with RegisterRender[ARenderItem] {
        override val default = null
    }
    private val multiBlockData = new RegisterNamespace[MultiBlockSingle]

    def registerBlock(id: Int, block: Block): Unit = {
        if (privateRegisterBlock(id, block)) {

            val item = new ItemBlock(block)

            if (privateRegisterItem(id, item)) {
                itemData.registerRender(id, new RenderItemBlock(item))
            }
        }
    }

    private def privateRegisterBlock(id: Int, block: Block): Boolean = {
        if (blockData.contains(block)) {
            println("Block \"" + block.name + "\" already register")
        } else {
            if (blockData.contains(id)) {
                println("Id \"" + id + "\" not single")
            } else {
                if (blockData.contains(block.name)) {
                    println("Name \"" + block.name + "\" not single")
                } else {
                    blockData.registerObject(id, block.name, block)
                    return true
                }
            }
        }
        false
    }

    def registerItem(id: Int, item: Item): Unit = {
        if (privateRegisterItem(id, item)) {
            itemData.registerRender(id, new RenderItemStandart(item))
        }
    }

    private def privateRegisterItem(id: Int, item: Item): Boolean = {
        if (itemData.contains(item)) {
            println("Item \"" + item.name + "\" already register")
        } else {
            if (itemData.contains(id)) {
                println("Id \"" + id + "\" not single")
            } else {
                if (itemData.contains(item.name)) {
                    println("Name \"" + item.name + "\" not single")
                } else {
                    itemData.registerObject(id, item.name, item)

                    return true
                }
            }
        }
        false
    }


    def getBlockFromItem(item: Item): Block = blockData.getObject(itemData.getIdByObject(item))


    def getItemFromBlock(block: Block): Item = itemData.getObject(blockData.getIdByObject(block))

    def getBlocks: Iterable[Block] = blockData.getObjects

    def getItems: Iterable[Item] = itemData.getObjects

    def getBlockById(id: Int): Block = blockData.getObject(id)

    def getItemById(id: Int): Item = itemData.getObject(id)

    def getBlockByName(name: String): Block = blockData.getObject(name)

    def registerBlockRender(block: Block, aRenderBlock: ARenderBlock): Unit = {
        val id: Int = getIdByBlock(block)
        if (id != -1) {
            blockData.registerRender(id, aRenderBlock)
        } else {
            println("Block +\"" + block.name + "\" not register")
        }
    }

    def getBlockRender(block: Block): ARenderBlock = blockData.getRender(getIdByBlock(block))

    def getIdByBlock(block: Block): Int = blockData.getIdByObject(block)

    def getItemRender(item: Item): ARenderItem = itemData.getRender(getIdByItem(item))

    def getIdByItem(item: Item): Int = itemData.getIdByObject(item)

    def getItemByName(name: String): Item = itemData.getObject(name)




    def registerMultiBlockSingle(multiBlock: MultiBlockSingle): Unit = {
        if (multiBlockData.contains(multiBlock)) {
            println("MultiBlockSingle +\"" + multiBlock.block.name + "\" already register")
        } else {
            val id = getIdByBlock(multiBlock.block)
            if (multiBlockData.contains(id)) {
                println("Id \"" + id + "\" not single")
            } else {
                val name = multiBlock.block.name
                if (multiBlockData.contains(name)) {
                    println("Name \"" + name + "\" not single")
                } else {
                    multiBlockData.registerObject(id, name, multiBlock)

                }
            }
        }
    }

    def getMultiBlockId(block: Block): Int = {
        val id = getIdByBlock(block)
        if (multiBlockData.contains(id)) {
            id
        } else {
            MultiBlock.id
        }
    }

    def getMultiBlock(id: Int): AMultiBlock = multiBlockData.getObject(id)

    def getMultiBlockByName(name: String): AMultiBlock = multiBlockData.getObject(name)

}
