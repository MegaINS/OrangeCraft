package ru.megains.game.block


import org.joml.Vector3f
import ru.megains.engine.graph.renderer.block.RenderBlockGlass
import ru.megains.engine.graph.renderer.texture.{TTextureRegister, TextureAtlas}
import ru.megains.game.blockdata.{BlockDirection, BlockSize, BlockWorldPos, MultiBlockPos}
import ru.megains.game.item.Item
import ru.megains.game.physics.{AxisAlignedBB, BlockAxisAlignedBB}
import ru.megains.game.register.{Blocks, GameRegister}
import ru.megains.game.util.RayTraceResult
import ru.megains.game.world.World

import scala.util.Random


class Block(val name: String) {


    var aTexture: TextureAtlas = _

    var textureName: String = ""

    def randomUpdate(world: World, blockPos: BlockWorldPos, rand: Random): Unit = {

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

    def getPhysicsBody: BlockAxisAlignedBB = BlockSize.FULL_AABB

    def getSelectedBoundingBox(pos: BlockWorldPos, offset: MultiBlockPos): AxisAlignedBB = BlockSize.FULL_AABB.sum(offset.floatX, offset.floatY, offset.floatZ)

    def getBoundingBox(pos: BlockWorldPos, offset: MultiBlockPos): AxisAlignedBB = getSelectedBoundingBox(pos, offset).sum(pos.worldX, pos.worldY, pos.worldZ)

    def collisionRayTrace(world: World, pos: BlockWorldPos, start: Vector3f, end: Vector3f, offset: MultiBlockPos): RayTraceResult = {


        val vec3d = new Vector3f(start).sub(pos.worldX, pos.worldY, pos.worldZ)
        val vec3d1 = new Vector3f(end).sub(pos.worldX, pos.worldY, pos.worldZ)
        val rayTraceResult = getSelectedBoundingBox(pos, offset).calculateIntercept(vec3d, vec3d1)
        if (rayTraceResult == null) {
            null
        } else {

            new RayTraceResult(rayTraceResult.hitVec.add(pos.worldX, pos.worldY, pos.worldZ), rayTraceResult.sideHit, new BlockWorldPos(pos, offset.x, offset.y, offset.z), this)
        }
    }

    def isAir: Boolean = false

    def getLayerRender: Int = 0

}

object Block {
    def getIdByBlock(block: Block) = {
        GameRegister.getIdByBlock(block)
    }

    def getBlockFromItem(item: Item) ={
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
        GameRegister.registerBlockRender(Blocks.glass, RenderBlockGlass)
    }


}

