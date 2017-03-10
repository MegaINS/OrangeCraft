package ru.megains.client.renderer

import java.awt.Color

import org.lwjgl.stb.{STBTTAlignedQuad, STBTruetype}
import org.lwjgl.system.MemoryStack
import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.font.Font
import ru.megains.client.renderer.mesh.{Mesh, MeshMaker}

import scala.collection.mutable


class FontRender(oc: OrangeCraft) {

    val ZPOS: Float = 0.0f
    //  val ascii: String = "font/ascii"
    //  val charWidth: Array[Int] = new Array[Int](256)
    //    for (i <- charWidth.indices) {
    //
    //        i match {
    //            case 32 | 116 => charWidth(i) = 5
    //            case 44 | 46 | 58 | 59 | 102 | 105 | 108 => charWidth(i) = 3
    //            case _ => charWidth(i) = 7
    //        }
    //
    //    }
    //  val BITMAP_W = 512
    //  val BITMAP_H = 512
    //   val ttfName = "OpenSans-Bold"
    //   val textureTTF = new TextureTTF(ttfName)
    //  oc.textureManager.loadTexture(ttfName,textureTTF)
    // val cdata: STBTTBakedChar.Buffer = textureTTF.cdata
    val fontsMap: mutable.HashMap[String, Font] = new mutable.HashMap[String, Font]

    //    def createStringGui(text: String, color: Color): Mesh = {
    //        createStringGuiSTD(text,color)
    //      //  createStringGuiTTF(text,color)
    //    }
    def createStringGui(text: String, color: Color, font: Font): Mesh = {
        val stack: MemoryStack = MemoryStack.stackPush
        val x = stack.floats(0.0f)
        val y = stack.floats(0.0f)
        val q = STBTTAlignedQuad.mallocStack(stack)
        val mm = MeshMaker
        mm.startMakeTriangles()
        mm.setTexture(font.name)
        mm.addColor(color)

        for (i <- text.indices) {
            val c = text.charAt(i)
            if (c == '\n') {
                y.put(0, y.get(0) + font.height)
                x.put(0, 0.0f)
            } else {
                STBTruetype.stbtt_GetBakedQuad(font.cdata, font.BITMAP_W, font.BITMAP_H, c - 32, x, y, q, true)
                mm.setCurrentIndex()
                mm.addVertexWithUV(q.x0, q.y1 * -1, ZPOS, q.s0, q.t1)
                mm.addVertexWithUV(q.x0, q.y0 * -1, ZPOS, q.s0, q.t0)
                mm.addVertexWithUV(q.x1, q.y0 * -1, ZPOS, q.s1, q.t0)
                mm.addVertexWithUV(q.x1, q.y1 * -1, ZPOS, q.s1, q.t1)
                mm.addIndex(1, 0, 3)
                mm.addIndex(2, 1, 3)
            }
        }
        if (stack != null) stack.close()
        mm.makeMesh()
    }

    //    def createStringGuiSTD(text: String, color: Color): Mesh = {
    //        val characters: Array[Char] = text.toCharArray
    //        val mm = MeshMaker
    //        mm.startMakeTriangles()
    //        mm.setTexture(ascii)
    //        mm.addColor(color)
    //        var startx: Float = 0
    //        val a: Float = 1f / 128f
    //        var b: Int = 0
    //        var u: Float = .0f
    //        var v: Float = .0f
    //        for (character <- characters) {
    //
    //            b = charWidth(character)
    //            v = character.toInt / 16
    //            u = character.toInt - 16 * v
    //            mm.setCurrentIndex()
    //            mm.addVertexWithUV(startx, 8, ZPOS, u * 8 * a, v * 8 * a)
    //            mm.addVertexWithUV(startx, 0.0f, ZPOS, u * 8 * a, (v * 8 + 8) * a)
    //            mm.addVertexWithUV(startx + b, 0.0f, ZPOS, (u * 8 + b) * a, (v * 8 + 8) * a)
    //            mm.addVertexWithUV(startx + b, 8, ZPOS, (u * 8 + b) * a, v * 8 * a)
    //            mm.addIndex(0,1,2)
    //            mm.addIndex(3,0,2)
    //            startx += b
    //        }
    //        mm.makeMesh()
    //    }


    //    def createStringGuiTTF(text: String, color: Color): Mesh ={
    //
    //            val stack: MemoryStack = MemoryStack.stackPush
    //
    //                val x = stack.floats(0.0f)
    //                val y = stack.floats(0.0f)
    //                val q = STBTTAlignedQuad.mallocStack(stack)
    //                val mm = MeshMaker
    //                mm.startMakeTriangles()
    //                mm.setTexture(ttfName)
    //                mm.addColor(color)
    //
    //                for(i<-text.indices){
    //                    val c = text.charAt(i)
    //                    if (c == '\n') {
    //                        y.put(0, y.get(0) + 24/*getFontHeight*/)
    //                        x.put(0, 0.0f)
    //                    }else{
    //                        STBTruetype.stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, x, y, q, true)
    //
    //                        mm.setCurrentIndex()
    //                        mm.addVertexWithUV(q.x0, q.y1* -1 , ZPOS, q.s0, q.t1)
    //                        mm.addVertexWithUV(q.x0, q.y0 * -1, ZPOS, q.s0, q.t0)
    //                        mm.addVertexWithUV(q.x1, q.y0* -1, ZPOS, q.s1, q.t0)
    //                        mm.addVertexWithUV(q.x1, q.y1* -1, ZPOS, q.s1, q.t1)
    //                        mm.addIndex(1,0,3)
    //                        mm.addIndex(2,1,3)
    //
    //
    //                    }
    //                }
    //                if (stack != null) stack.close()
    //                mm.makeMesh()
    //
    //    }

    def loadFont(name: String): Font = {
        if (fontsMap.contains(name)) fontsMap(name)
        else {
            val font = new Font(name)
            oc.textureManager.loadTexture(name, font)
            fontsMap += name -> font
            font
        }
    }











}
