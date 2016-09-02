package ru.megains.game.blockdata

class BlockWorldPos(val worldX: Int, val worldY: Int, val worldZ: Int, val blockX: BlockSize, val blockY: BlockSize, val blockZ: BlockSize) {

    val multiPos = new MultiBlockPos(blockX, blockY, blockZ)


    def add(pos: MultiBlockPos): BlockWorldPos = {
        new BlockWorldPos(worldX, worldY, worldZ, pos.x, pos.y, pos.z)
    }

    def this(worldX: Int, worldY: Int, worldZ: Int) {
        this(worldX, worldY, worldZ, BlockSize.Zero, BlockSize.Zero, BlockSize.Zero)
    }

    def this(pos: BlockWorldPos, blockX: BlockSize, blockY: BlockSize, blockZ: BlockSize) {
        this(pos.worldX, pos.worldY, pos.worldZ, blockX, blockY, blockZ)
    }

    def this(pos: BlockWorldPos) {
        this(pos.worldX, pos.worldY, pos.worldZ, BlockSize.Zero, BlockSize.Zero, BlockSize.Zero)
    }

    def sum(direction: BlockDirection) = new BlockWorldPos(worldX + direction.x, worldY + direction.y, worldZ + direction.z, blockX, blockY, blockZ)

    def sum(blockPos: BlockWorldPos) = new BlockWorldPos(worldX + blockPos.worldX, worldY + blockPos.worldY, worldZ + blockPos.worldZ, blockX, blockY, blockZ)

    def sum(x: Int, y: Int, z: Int) = new BlockWorldPos(worldX + x, worldY + y, worldZ + z, blockX, blockY, blockZ)
}

