package ru.megains.engine.graph.renderer.block

import ru.megains.engine.graph.renderer.mesh.MeshMaker
import ru.megains.engine.graph.renderer.texture.TextureAtlas



object RenderBlock {

    val mm = MeshMaker.getMeshMaker


    def renderSideUp(minX:Float,maxX:Float,maxY:Float,minZ:Float,maxZ:Float,texture:TextureAtlas): Unit ={

        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageX = (minX+maxX)/2
        val averageZ = (minZ+maxZ)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2

        mm.setCurrentIndex()
        mm.addNormals(0, 1, 0)
        mm.addVertexWithUV(minX, maxY, minZ,maxU, maxV)
        mm.addVertexWithUV(minX, maxY, maxZ,maxU, minV)
        mm.addVertexWithUV(maxX, maxY, minZ,minU, maxV)
        mm.addVertexWithUV(maxX, maxY, maxZ,minU, minV)
        mm.addVertexWithUV(averageX, maxY, averageZ,averageU, averageV)
        mm.addIndex(0,1,4)
        mm.addIndex(3, 2,4)
        mm.addIndex(1, 3,4)
        mm.addIndex(2, 0,4)
    }

    def renderSideDown(minX:Float,maxX:Float,minY:Float,minZ:Float,maxZ:Float,texture:TextureAtlas): Unit ={

        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageX = (minX+maxX)/2
        val averageZ = (minZ+maxZ)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2

        mm.setCurrentIndex()
        mm.addNormals(0, -1, 0)
        mm.addVertexWithUV(minX, minY, minZ,minU, maxV)
        mm.addVertexWithUV(minX, minY, maxZ,minU, minV)
        mm.addVertexWithUV(maxX, minY, minZ,maxU, maxV)
        mm.addVertexWithUV(maxX, minY, maxZ,maxU, minV)
        mm.addVertexWithUV(averageX, minY, averageZ,averageU, averageV)
        mm.addIndex(1, 0, 4)
        mm.addIndex(2, 3, 4)
        mm.addIndex(3, 1, 4)
        mm.addIndex(0, 2, 4)
    }

    def renderSideWest(minX:Float,minY:Float,maxY:Float,minZ:Float,maxZ:Float,texture:TextureAtlas): Unit ={
        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageY = (minY+maxY)/2
        val averageZ = (minZ+maxZ)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2


        mm.setCurrentIndex()
        mm.addNormals(-1, 0, 0)
        mm.addVertexWithUV(minX, minY, minZ,minU, maxV)
        mm.addVertexWithUV(minX, minY, maxZ,maxU, maxV)
        mm.addVertexWithUV(minX, maxY, minZ,minU, minV)
        mm.addVertexWithUV(minX, maxY, maxZ,maxU, minV)
        mm.addVertexWithUV(minX, averageY, averageZ,averageU, averageV)
        mm.addIndex(0,1,4)
        mm.addIndex(1, 3, 4)
        mm.addIndex(3, 2, 4)
        mm.addIndex(2, 0, 4)

    }

    def renderSideEast(maxX:Float,minY:Float,maxY:Float,minZ:Float,maxZ:Float,texture:TextureAtlas): Unit ={
        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageY = (minY+maxY)/2
        val averageZ = (minZ+maxZ)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2



        mm.setCurrentIndex()
        mm.addNormals(1,0,0)
        mm.addVertexWithUV(maxX, minY, minZ,maxU, maxV)
        mm.addVertexWithUV(maxX, minY, maxZ,minU, maxV)
        mm.addVertexWithUV(maxX, maxY, minZ,maxU, minV)
        mm.addVertexWithUV(maxX, maxY, maxZ,minU, minV)
        mm.addVertexWithUV(maxX, averageY, averageZ,averageU, averageV)
        mm.addIndex(1, 0, 4)
        mm.addIndex(3, 1, 4)
        mm.addIndex(2, 3, 4)
        mm.addIndex(0, 2, 4)
    }

    def renderSideSouth(minX:Float,maxX:Float,minY:Float,maxY:Float,maxZ:Float,texture:TextureAtlas): Unit ={

        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageX = (minX+maxX)/2
        val averageY = (minY+maxY)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2

        mm.setCurrentIndex()
        mm.addNormals(0, 0, 1)
        mm.addVertexWithUV(minX, minY, maxZ,minU,maxV)
        mm.addVertexWithUV(minX, maxY, maxZ,minU,minV)
        mm.addVertexWithUV(maxX, minY, maxZ,maxU,maxV)
        mm.addVertexWithUV(maxX, maxY, maxZ,maxU,minV)
        mm.addVertexWithUV(averageX, averageY, maxZ,averageU,averageV)
        mm.addIndex(1, 0, 4)
        mm.addIndex(4, 0, 2)
        mm.addIndex(4, 2, 3)
        mm.addIndex(3, 1, 4)
    }

    def renderSideNorth(minX:Float,maxX:Float,minY:Float,maxY:Float,minZ:Float,texture:TextureAtlas): Unit ={

        val minU = texture.minU
        val maxU = texture.maxU
        val minV = texture.minV
        val maxV = texture.maxV
        val averageX = (minX+maxX)/2
        val averageY = (minY+maxY)/2
        val averageU = (minU+maxU)/2
        val averageV = (minV+maxV)/2

        mm.setCurrentIndex()
        mm.addNormals(0, 0, -1)
        mm.addVertexWithUV(minX, minY, minZ,maxU,maxV)
        mm.addVertexWithUV(minX, maxY, minZ,maxU,minV)
        mm.addVertexWithUV(maxX, minY, minZ,minU,maxV)
        mm.addVertexWithUV(maxX, maxY, minZ,minU,minV)
        mm.addVertexWithUV(averageX, averageY, minZ,averageU,averageV)
        mm.addIndex(0, 1, 4)
        mm.addIndex(0, 4, 2)
        mm.addIndex(2, 4, 3)
        mm.addIndex(1, 3, 4)
    }


}
