package ru.megains.engine.graph.renderer.block

import ru.megains.engine.graph.renderer.api.ARenderBlock
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockWorldPos, MultiBlockPos}
import ru.megains.game.world.World


object RenderBlockStandart extends ARenderBlock{



  override def render(block: Block, world: World, posWorld: BlockWorldPos, posRender: BlockWorldPos,offset:MultiBlockPos): Unit = {


    val AABB = block.getSelectedBoundingBox(posWorld,offset).sum(posRender.worldX,posRender.worldY,posRender.worldZ)
    val minX = AABB.getMinX
    val minY = AABB.getMinY
    val minZ = AABB.getMinZ
    val maxX = AABB.getMaxX
    val maxY = AABB.getMaxY
    val maxZ = AABB.getMaxZ



    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.SOUTH))) {
      RenderBlock.renderSideSouth(minX,maxX,minY,maxY,maxZ,block.getATexture(BlockDirection.SOUTH))
    }

    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.NORTH))) {
      RenderBlock.renderSideNorth(minX,maxX,minY,maxY,minZ,block.getATexture(BlockDirection.NORTH))
    }

    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.DOWN))) {
      RenderBlock.renderSideDown(minX,maxX,minY,minZ,maxZ,block.getATexture(BlockDirection.DOWN))
    }

    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.UP))) {
      RenderBlock.renderSideUp(minX,maxX,maxY,minZ,maxZ,block.getATexture(BlockDirection.UP))
    }

    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.WEST))) {
      RenderBlock.renderSideWest(minX,minY,maxY,minZ,maxZ,block.getATexture(BlockDirection.WEST))
    }

    if (!world.isOpaqueCube(posWorld.sum(BlockDirection.EAST))) {
      RenderBlock.renderSideEast(maxX,minY,maxY,minZ,maxZ,block.getATexture(BlockDirection.EAST))
    }

  }
}
