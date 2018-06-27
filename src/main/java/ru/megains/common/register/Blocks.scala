package ru.megains.common.register

import ru.megains.client.renderer.block.RenderBlockGlass
import ru.megains.common.block.TEST.BlockMicroTest
import ru.megains.common.block._
import ru.megains.common.item.Item


object Blocks {


    lazy val air: Block = GameRegister.getBlockByName("air")
    lazy val dirt: Block = GameRegister.getBlockByName("dirt")
    lazy val grass: Block = GameRegister.getBlockByName("grass")
    lazy val stone: Block = GameRegister.getBlockByName("stone")
    lazy val glass: Block = GameRegister.getBlockByName("glass")
    lazy val micro1: Block = GameRegister.getBlockByName("micro0")
    lazy val micro2: Block = GameRegister.getBlockByName("micro1")
    lazy val micro3: Block = GameRegister.getBlockByName("micro2")
    lazy val micro4: Block = GameRegister.getBlockByName("micro3")

    //  lazy val macro1: Block = GameRegister.getBlockByName("macro0")
    // lazy val macro2: Block = GameRegister.getBlockByName("macro1")
    // lazy val macro3: Block = GameRegister.getBlockByName("macro2")


    lazy val blockMeta: Block = GameRegister.getBlockByName("blockMeta")

    def getIdByBlock(block: Block): Int = {
        GameRegister.getIdByBlock(block)
    }

    def getBlockFromItem(item: Item): Block = {
        GameRegister.getBlockFromItem(item)
    }

    def getBlockById(id: Int): Block = {
        val block: Block = GameRegister.getBlockById(id)
        if (block == null) {
            Blocks.air
        } else {
            block
        }
    }

    def initBlocks(): Unit = {

        GameRegister.registerBlock(1, new BlockAir("air"))
        GameRegister.registerBlock(2, new Block("stone"))
        GameRegister.registerBlock(3, new Block("dirt"))
        GameRegister.registerBlock(4, new BlockGrass("grass"))
        GameRegister.registerBlock(5, new BlockGlass("glass"))
        GameRegister.registerBlock(6, new BlockMicroTest("micro0", 0))
        GameRegister.registerBlock(7, new BlockMicroTest("micro1", 1))
        GameRegister.registerBlock(8, new BlockMicroTest("micro2", 2))

        GameRegister.registerBlock(9, new Block("brick"))
        GameRegister.registerBlock(10, new Block("sand"))
        GameRegister.registerBlock(11, new Block("cobblestone"))
        GameRegister.registerBlock(12, new Block("planks_oak"))
        GameRegister.registerBlock(13, new Block("leaves_oak"))

        // GameRegister.registerBlock(14, new MacroBlockTest("macro0", 0))
        // GameRegister.registerBlock(15, new MacroBlockTest("macro1", 1))
        // GameRegister.registerBlock(16, new MacroBlockTest("macro2", 2))
        // GameRegister.registerBlock(18, new MacroBlockTest("macro3", 3))
        GameRegister.registerBlock(19, new BlockMicroTest("micro3", 3))
        GameRegister.registerBlock(17, new BlockMeta("blockMeta"))
        GameRegister.registerBlockRender(Blocks.glass, RenderBlockGlass)
    }
}
