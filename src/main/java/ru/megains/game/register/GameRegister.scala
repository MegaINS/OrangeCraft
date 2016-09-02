package ru.megains.game.register

import ru.megains.engine.graph.renderer.api.{ARenderBlock, ARenderItem}
import ru.megains.engine.graph.renderer.block.RenderBlockStandart
import ru.megains.engine.graph.renderer.item.{RenderItemBlock, RenderItemStandart}
import ru.megains.game.block.Block
import ru.megains.game.item.{Item, ItemBlock}
import ru.megains.game.multiblock.{AMultiBlock, MultiBlock, MultiBlockSingle}

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

    def registerItem(id: Int, item: Item): Unit = {
        if (privateRegisterItem(id, item)) {
            itemData.registerRender(id, new RenderItemStandart(item))
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


    def getItemFromBlock(block: Block): Item = itemData.getObject(blockData.getIdByObject(block))

    def getBlocks = blockData.getObjects

    def getBlockById(id: Int): Block = blockData.getObject(id)

    def getIdByBlock(block: Block): Int = blockData.getIdByObject(block)

    def getIdByItem(item: Item): Int = itemData.getIdByObject(item)

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

    def getItemRender(item: Item): ARenderItem = itemData.getRender(getIdByItem(item))

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

    def getMultiBlockId(block: Block) = {
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
