package ru.megains.common.block


class BlockGlass(name: String) extends Block(name) {

    override val maxHp: Short = 12
    override def isOpaqueCube: Boolean = false

}
