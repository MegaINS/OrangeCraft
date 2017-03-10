package ru.megains.common.block.blockdata

class BlockPos(val worldX: Int, val worldY: Int, val worldZ: Int, val blockX: BlockSize, val blockY: BlockSize, val blockZ: BlockSize) {

    val multiPos = new MultiBlockPos(blockX, blockY, blockZ)


    def add(pos: MultiBlockPos): BlockPos = {
        new BlockPos(worldX, worldY, worldZ, pos.x, pos.y, pos.z)
    }

    def this(worldX: Int, worldY: Int, worldZ: Int) {
        this(worldX, worldY, worldZ, BlockSize.S0, BlockSize.S0, BlockSize.S0)
    }

    def this(pos: BlockPos, blockX: BlockSize, blockY: BlockSize, blockZ: BlockSize) {
        this(pos.worldX, pos.worldY, pos.worldZ, blockX, blockY, blockZ)
    }

    def this(pos: BlockPos) {
        this(pos.worldX, pos.worldY, pos.worldZ, BlockSize.S0, BlockSize.S0, BlockSize.S0)
    }


    def getS0 = new BlockPos(this)

    def sum(direction: BlockDirection) = new BlockPos(worldX + direction.x, worldY + direction.y, worldZ + direction.z, blockX, blockY, blockZ)

    def sum(blockPos: BlockPos) = new BlockPos(worldX + blockPos.worldX, worldY + blockPos.worldY, worldZ + blockPos.worldZ, blockX, blockY, blockZ)

    def sum(x: Int, y: Int, z: Int) = new BlockPos(worldX + x, worldY + y, worldZ + z, blockX, blockY, blockZ)

    def eq(pos: BlockPos): Boolean = {


        pos != null & pos.worldX == worldX && pos.worldY == worldY && pos.worldZ == worldZ && pos.multiPos == multiPos
    }
}

