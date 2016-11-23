package ru.megains.common.block


import org.joml.{Vector3d, Vector3f}
import ru.megains.client.renderer.block.RenderBlockGlass
import ru.megains.client.renderer.texture.{TTextureRegister, TextureAtlas}
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos, BlockSize, MultiBlockPos}
import ru.megains.common.entity.EntityLivingBase
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.item.{Item, ItemStack}
import ru.megains.common.physics.{AxisAlignedBB, BlockAxisAlignedBB}
import ru.megains.common.register.{Blocks, GameRegister}
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

import scala.util.Random


class Block(val name: String) {


    var aTexture: TextureAtlas = _

    var textureName: String = ""

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

    def isFullBlock: Boolean = true


    def isOpaqueCube: Boolean = true

    def onBlockActivated(worldIn: World, pos: BlockPos, playerIn: EntityPlayer, heldItem: ItemStack, side: BlockDirection, hitX: Float, hitY: Float, hitZ: Float): Boolean = false

    def getPhysicsBody: BlockAxisAlignedBB = BlockSize.FULL_AABB

    def getBoundingBox(pos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = getSelectedBoundingBox(pos, offset).sum(pos.worldX, pos.worldY, pos.worldZ)

    def getSelectedBoundingBox(pos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = BlockSize.FULL_AABB.sum(offset.floatX, offset.floatY, offset.floatZ)

    def collisionRayTrace(world: World, pos: BlockPos, start: Vector3d, end: Vector3d, offset: MultiBlockPos): RayTraceResult = {


        val vec3d = new Vector3f(start.x toFloat, start.y toFloat, start.z toFloat).sub(pos.worldX, pos.worldY, pos.worldZ)
        val vec3d1 = new Vector3f(end.x toFloat, end.y toFloat, end.z toFloat).sub(pos.worldX, pos.worldY, pos.worldZ)
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
}

object Block {
    def getIdByBlock(block: Block) = {
        GameRegister.getIdByBlock(block)
    }

    def getBlockFromItem(item: Item) = {
        GameRegister.getBlockFromItem(item)
    }

    def getBlockById(id: Int): Block = {
        val block: Block = GameRegister.getBlockById(id)
        if (block == null) {
            Blocks.air
        } else {
            block
        }
    }

    def initBlocks(): Unit = {

        GameRegister.registerBlock(1, new BlockAir("air"))
        GameRegister.registerBlock(2, new Block("stone"))
        GameRegister.registerBlock(3, new Block("dirt"))
        GameRegister.registerBlock(4, new BlockGrass("grass"))
        GameRegister.registerBlock(5, new BlockGlass("glass"))
        GameRegister.registerBlock(6, new BlockMicroTest("micro0", 0))
        GameRegister.registerBlock(7, new BlockMicroTest("micro1", 1))
        GameRegister.registerBlock(8, new BlockMicroTest("micro2", 2))

        GameRegister.registerBlock(9, new Block("brick"))
        GameRegister.registerBlock(10, new Block("sand"))
        GameRegister.registerBlock(11, new Block("cobblestone"))
        GameRegister.registerBlock(12, new Block("planks_oak"))
        GameRegister.registerBlock(13, new Block("leaves_oak"))
        GameRegister.registerBlockRender(Blocks.glass, RenderBlockGlass)
    }


}
