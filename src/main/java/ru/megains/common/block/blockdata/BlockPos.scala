package ru.megains.common.block.blockdata

class BlockPos(val worldX: Int, val worldY: Int, val worldZ: Int, val multiPos: MultiBlockPos) {




    def add(pos: MultiBlockPos): BlockPos = {
        new BlockPos(worldX, worldY, worldZ, pos)
    }

    def this(worldX: Int, worldY: Int, worldZ: Int) {
        this(worldX, worldY, worldZ, MultiBlockPos.default)
    }

    def this(pos: BlockPos, blockX: BlockSize, blockY: BlockSize, blockZ: BlockSize) {
        this(pos.worldX, pos.worldY, pos.worldZ, new MultiBlockPos(blockX, blockY, blockZ))
    }

    def this(worldX: Int, worldY: Int, worldZ: Int, blockX: BlockSize, blockY: BlockSize, blockZ: BlockSize) {
        this(worldX, worldY, worldZ, new MultiBlockPos(blockX, blockY, blockZ))
    }

    def this(pos: BlockPos) {
        this(pos.worldX, pos.worldY, pos.worldZ, MultiBlockPos.default)
    }


    def getS0 = new BlockPos(this)

    def sum(direction: BlockDirection) = new BlockPos(worldX + direction.x, worldY + direction.y, worldZ + direction.z, multiPos)

    def sum(blockPos: BlockPos) = new BlockPos(worldX + blockPos.worldX, worldY + blockPos.worldY, worldZ + blockPos.worldZ, multiPos)

    def sum(x: Int, y: Int, z: Int) = new BlockPos(worldX + x, worldY + y, worldZ + z, multiPos)

    def eq(pos: BlockPos): Boolean = {


        pos != null & pos.worldX == worldX && pos.worldY == worldY && pos.worldZ == worldZ && pos.multiPos == multiPos
    }
}

