package ru.megains.client.renderer.block


import ru.megains.client.renderer.api.ARenderBlock
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos, MultiBlockPos}
import ru.megains.common.multiblock.AMultiBlock
import ru.megains.common.register.MultiBlocks
import ru.megains.common.world.World


object RenderBlockGlass extends ARenderBlock {
    override def render(block: Block, world: World, posWorld: BlockPos, posRender: BlockPos, offset: MultiBlockPos): Boolean = {

        val AABB = block.getSelectedBoundingBox(posWorld, offset).sum(posRender.worldX, posRender.worldY, posRender.worldZ)
        val minX = AABB.getMinX
        val minY = AABB.getMinY
        val minZ = AABB.getMinZ
        val maxX = AABB.getMaxX
        val maxY = AABB.getMaxY
        val maxZ = AABB.getMaxZ
        var block1: AMultiBlock = null
        var isRender = false
        var pos: BlockPos = null

        pos = posWorld.sum(BlockDirection.SOUTH)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideSouth(minX, maxX, minY, maxY, maxZ, block.getATexture(posWorld, BlockDirection.SOUTH, world))
            isRender = true
        }
        pos = posWorld.sum(BlockDirection.NORTH)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideNorth(minX, maxX, minY, maxY, minZ, block.getATexture(posWorld, BlockDirection.NORTH, world))
            isRender = true
        }
        pos = posWorld.sum(BlockDirection.DOWN)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideDown(minX, maxX, minY, minZ, maxZ, block.getATexture(posWorld, BlockDirection.DOWN, world))
            isRender = true
        }
        pos = posWorld.sum(BlockDirection.UP)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideUp(minX, maxX, maxY, minZ, maxZ, block.getATexture(posWorld, BlockDirection.UP, world))
            isRender = true
        }
        pos = posWorld.sum(BlockDirection.WEST)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideWest(minX, minY, maxY, minZ, maxZ, block.getATexture(posWorld, BlockDirection.WEST, world))
            isRender = true
        }
        pos = posWorld.sum(BlockDirection.EAST)
        block1 = world.getMultiBlock(pos)
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideEast(maxX, minY, maxY, minZ, maxZ, block.getATexture(posWorld, BlockDirection.EAST, world))
            isRender = true
        }

        isRender
    }
}
