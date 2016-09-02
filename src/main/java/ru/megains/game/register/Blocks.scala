package ru.megains.game.register

import ru.megains.game.block.Block
import ru.megains.game.multiblock.AMultiBlock


object Blocks {


    val air: Block = GameRegister.getBlockByName("air")
    val dirt: Block = GameRegister.getBlockByName("dirt")
    val grass: Block = GameRegister.getBlockByName("grass")
    val stone: Block = GameRegister.getBlockByName("stone")
    val glass: Block = GameRegister.getBlockByName("glass")
    val micro1: Block = GameRegister.getBlockByName("micro0")
    val micro2: Block = GameRegister.getBlockByName("micro1")
    val micro3: Block = GameRegister.getBlockByName("micro2")
    lazy val multiAir: AMultiBlock = GameRegister.getMultiBlockByName("air")
    lazy val multiGlass: AMultiBlock = GameRegister.getMultiBlockByName("glass")

}
