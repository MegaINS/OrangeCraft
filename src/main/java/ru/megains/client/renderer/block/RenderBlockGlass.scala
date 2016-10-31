package ru.megains.client.renderer.block


import ru.megains.client.renderer.api.ARenderBlock
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockPos, MultiBlockPos}
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.register.MultiBlocks
import ru.megains.game.world.World


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



        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.SOUTH))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideSouth(minX, maxX, minY, maxY, maxZ, block.getATexture(BlockDirection.SOUTH))
            isRender = true
        }
        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.NORTH))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideNorth(minX, maxX, minY, maxY, minZ, block.getATexture(BlockDirection.NORTH))
            isRender = true
        }
        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.DOWN))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideDown(minX, maxX, minY, minZ, maxZ, block.getATexture(BlockDirection.DOWN))
            isRender = true
        }
        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.UP))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideUp(minX, maxX, maxY, minZ, maxZ, block.getATexture(BlockDirection.UP))
            isRender = true
        }
        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.WEST))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideWest(minX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.WEST))
            isRender = true
        }
        block1 = world.getMultiBlock(posWorld.sum(BlockDirection.EAST))
        if (!block1.isOpaqueCube && block1 != MultiBlocks.glass) {
            RenderBlock.renderSideEast(maxX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.EAST))
            isRender = true
        }

        isRender
    }
}
