package ru.megains.common.block


import org.joml.Vector3d
import ru.megains.client.renderer.texture.{TTextureRegister, TextureAtlas}
import ru.megains.common.block.blockdata._
import ru.megains.common.entity.EntityLivingBase
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.item.ItemStack
import ru.megains.common.physics.{AxisAlignedBB, BlockAxisAlignedBB}
import ru.megains.common.util.{RayTraceResult, Vec3f}
import ru.megains.common.world.World

import scala.util.Random


class Block(val name: String) {


    val maxHp: Int = 100

    var aTexture: TextureAtlas = _

    var textureName: String = ""

    val format: BlockFormat = BlockFormat.STANDART

    def randomUpdate(world: World, blockPos: BlockPos, rand: Random): Unit = {

    }

    def onBlockPlacedBy(worldIn: World, pos: BlockPos, placer: EntityLivingBase, stack: ItemStack) {
    }

    def getATexture(blockDirection: BlockDirection): TextureAtlas = aTexture

    def setTextureName(textureName: String): Block = {
        this.textureName = textureName
        this
    }

    def registerTexture(textureRegister: TTextureRegister): Unit = {
        aTexture = textureRegister.registerTexture(name)
    }

    // def isFullBlock: Boolean = true


    def isOpaqueCube: Boolean = true

    def onBlockActivated(worldIn: World, pos: BlockPos, playerIn: EntityPlayer, heldItem: ItemStack, side: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): Boolean = false

    def getPhysicsBody: BlockAxisAlignedBB = BlockSize.FULL_AABB

    def getBoundingBox(pos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = getSelectedBoundingBox(pos, offset).sum(pos.worldX, pos.worldY, pos.worldZ)

    def getSelectedBoundingBox(pos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = BlockSize.FULL_AABB.sum(offset.floatX, offset.floatY, offset.floatZ)

    def collisionRayTrace(world: World, pos: BlockPos, start: Vector3d, end: Vector3d, offset: MultiBlockPos): RayTraceResult = {


        val vec3d: Vec3f = new Vec3f(start.x toFloat, start.y toFloat, start.z toFloat).sub(pos.worldX, pos.worldY, pos.worldZ)
        val vec3d1: Vec3f = new Vec3f(end.x toFloat, end.y toFloat, end.z toFloat).sub(pos.worldX, pos.worldY, pos.worldZ)
        val rayTraceResult = getSelectedBoundingBox(pos, offset).calculateIntercept(vec3d, vec3d1)
        if (rayTraceResult == null) {
            null
        } else {

            new RayTraceResult(rayTraceResult.hitVec.add(pos.worldX, pos.worldY, pos.worldZ), rayTraceResult.sideHit, new BlockPos(pos, offset.x, offset.y, offset.z), this)
        }
    }

    def isAir: Boolean = false

    def getLayerRender: Int = 0

    def removedByPlayer(world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean = {
        onBlockHarvested(world, pos, player)
        world.setAirBlock(pos)
    }

    def onBlockHarvested(worldIn: World, pos: BlockPos, player: EntityPlayer) {
    }

    def onBlockDestroyedByPlayer(worldIn: World, pos: BlockPos) {
    }

    def getSelectPosition(worldIn: World, objectMouseOver: RayTraceResult): BlockPos = {
        val posTarget: BlockPos = objectMouseOver.getBlockWorldPos.getS0
        val posSet: BlockPos = posTarget.sum(objectMouseOver.sideHit)
        if (worldIn.isAirBlock(posSet)) posSet
        else null
    }


}


