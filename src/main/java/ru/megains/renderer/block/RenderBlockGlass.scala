package ru.megains.renderer.block


import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockWorldPos, MultiBlockPos}
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.register.Blocks
import ru.megains.game.world.World
import ru.megains.renderer.api.ARenderBlock


object RenderBlockGlass extends ARenderBlock {
    override def render(block: Block, world: World, posWorld: BlockWorldPos, posRender: BlockWorldPos, offset: MultiBlockPos): Unit = {

        val AABB = block.getSelectedBoundingBox(posWorld, offset).sum(posRender.worldX, posRender.worldY, posRender.worldZ)
        val minX = AABB.getMinX
        val minY = AABB.getMinY
        val minZ = AABB.getMinZ
        val maxX = AABB.getMaxX
        val maxY = AABB.getMaxY
        val maxZ = AABB.getMaxZ
        var block1: AMultiBlock = null




        block1 = world.getBlock(posWorld.sum(BlockDirection.SOUTH))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideSouth(minX, maxX, minY, maxY, maxZ, block.getATexture(BlockDirection.SOUTH))
        }
        block1 = world.getBlock(posWorld.sum(BlockDirection.NORTH))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideNorth(minX, maxX, minY, maxY, minZ, block.getATexture(BlockDirection.NORTH))
        }
        block1 = world.getBlock(posWorld.sum(BlockDirection.DOWN))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideDown(minX, maxX, minY, minZ, maxZ, block.getATexture(BlockDirection.DOWN))
        }
        block1 = world.getBlock(posWorld.sum(BlockDirection.UP))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideUp(minX, maxX, maxY, minZ, maxZ, block.getATexture(BlockDirection.UP))
        }
        block1 = world.getBlock(posWorld.sum(BlockDirection.WEST))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideWest(minX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.WEST))
        }
        block1 = world.getBlock(posWorld.sum(BlockDirection.EAST))
        if (!block1.isOpaqueCube && block1 != Blocks.multiGlass) {
            RenderBlock.renderSideEast(maxX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.EAST))
        }


    }
}
