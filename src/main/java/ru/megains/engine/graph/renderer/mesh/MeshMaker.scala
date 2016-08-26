package ru.megains.engine.graph.renderer.mesh

import org.lwjgl.opengl.GL11._


import scala.collection.mutable.ArrayBuffer


object MeshMaker {


    private var posArray:ArrayBuffer[Float] = ArrayBuffer[Float]()
    private var colourArray: ArrayBuffer[Float] = ArrayBuffer[Float]()
    private var textCordsArray: ArrayBuffer[Float] = ArrayBuffer[Float]()
    private var normalsArray: ArrayBuffer[Float] = ArrayBuffer[Float]()
    private var indicesArray: ArrayBuffer[Int] = ArrayBuffer[Int]()

    private var colorR: Float = 1
    private var colorG: Float = 1
    private var colorB: Float = 1
    private var textureU: Float = 0
    private var textureV: Float = 0
    private var normalX: Float = 0
    private var normalY: Float = 0
    private var normalZ: Float = 0
    private var currentIndex:Int = 0
    private var vertexCount:Int = 0
    private var makeMode:Int = 0
    private var isStartMake:Boolean = false
    private var isNormals:Boolean = false
    private var textureName:String = ""

    def getMeshMaker = this
    def startMakeTriangles(): Unit ={
        startMake(GL_TRIANGLES)
    }

    def startMake(mode:Int): Unit ={
        if(isStartMake){
            sys.error("MeshMaker has already started")
        }else{
            isStartMake = true
            reset()
            makeMode = mode
        }
    }

    def setTexture(textureName:String): Unit ={
        this.textureName = textureName
}
    def setCurrentIndex(): Unit = {
        currentIndex = vertexCount
    }

    def addIndex(index: Int*) {
        index.foreach(indicesArray += _ + currentIndex)
    }
    def addIndex(index: Int) {
        indicesArray :+= index + currentIndex
    }
    def addNormals(xn: Float, yn: Float, zn: Float) {
        normalX = xn
        normalY = yn
        normalZ = zn
        isNormals = true
    }

    def addVertexWithUV(x: Float, y: Float, z: Float, u: Float, v: Float) {
        addTextureUV(u, v)
        addVertex(x, y, z)
    }

    def addVertex(x: Float, y: Float, z: Float) {
        textCordsArray  +=(textureU,textureV)
        colourArray += (colorR,colorG,colorB)
        normalsArray += (normalX,normalY,normalZ)
        posArray += (x,y,z)
        vertexCount += 1
    }

    def addColor(r: Float, g: Float, b: Float) {
        colorR = r
        colorG = g
        colorB = b
    }

    def addTextureUV(u: Float, v: Float) {
        textureU = u
        textureV = v
    }

    def reset(): Unit ={

        posArray.clear()
        colourArray.clear()
        textCordsArray.clear()
        normalsArray.clear()
        indicesArray.clear()
        colorR  = 1
        colorG  = 1
        colorB  = 1
        textureV = 0
        textureU = 0
        normalX  = 0
        normalY  = 0
        normalZ  = 0
        makeMode =0
        currentIndex =0
        vertexCount =0
        isNormals = false
        textureName = ""

    }

    def makeMesh(): Mesh ={
        if(!isStartMake){
            sys.error("MeshMaker not started")
        }else{
            var mesh:Mesh = null
            if(isNormals){
                mesh = new MeshTextureNormals(makeMode,textureName,indicesArray.toArray, posArray.toArray,colourArray.toArray,textCordsArray.toArray,normalsArray.toArray)
            }else if(textureName != ""){
                mesh = new MeshTexture(makeMode,textureName,indicesArray.toArray, posArray.toArray,colourArray.toArray,textCordsArray.toArray)
            }else{
                mesh = new Mesh(makeMode,indicesArray.toArray, posArray.toArray,colourArray.toArray)
            }
            isStartMake = false
            reset()
            mesh
        }
    }


}
