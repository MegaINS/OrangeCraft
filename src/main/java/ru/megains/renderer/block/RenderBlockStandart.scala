package ru.megains.renderer.block

import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockWorldPos, MultiBlockPos}
import ru.megains.game.world.World
import ru.megains.renderer.api.ARenderBlock


object RenderBlockStandart extends ARenderBlock {


    override def render(block: Block, world: World, posWorld: BlockWorldPos, posRender: BlockWorldPos, offset: MultiBlockPos): Boolean = {


        val AABB = block.getSelectedBoundingBox(posWorld, offset).sum(posRender.worldX, posRender.worldY, posRender.worldZ)
        val minX = AABB.getMinX
        val minY = AABB.getMinY
        val minZ = AABB.getMinZ
        val maxX = AABB.getMaxX
        val maxY = AABB.getMaxY
        val maxZ = AABB.getMaxZ
        var isRender = false


        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.SOUTH))) {
            RenderBlock.renderSideSouth(minX, maxX, minY, maxY, maxZ, block.getATexture(BlockDirection.SOUTH))
            isRender = true
        }

        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.NORTH))) {
            RenderBlock.renderSideNorth(minX, maxX, minY, maxY, minZ, block.getATexture(BlockDirection.NORTH))
            isRender = true
        }

        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.DOWN))) {
            RenderBlock.renderSideDown(minX, maxX, minY, minZ, maxZ, block.getATexture(BlockDirection.DOWN))
            isRender = true
        }

        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.UP))) {
            RenderBlock.renderSideUp(minX, maxX, maxY, minZ, maxZ, block.getATexture(BlockDirection.UP))
            isRender = true
        }

        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.WEST))) {
            RenderBlock.renderSideWest(minX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.WEST))
            isRender = true
        }

        if (!world.isOpaqueCube(posWorld.sum(BlockDirection.EAST))) {
            RenderBlock.renderSideEast(maxX, minY, maxY, minZ, maxZ, block.getATexture(BlockDirection.EAST))
            isRender = true
        }
        isRender
    }
}
