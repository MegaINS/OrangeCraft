package ru.megains.renderer

import java.awt.Color

import ru.megains.renderer.mesh.{Mesh, MeshMaker}

class FontRender {

    val ZPOS: Float = 0.0f
    val ascii: String = "font/ascii"
    val charWidth: Array[Int] = new Array[Int](256)
    for (i <- charWidth.indices) {

        i match {
            case 32 | 116 => charWidth(i) = 5
            case 44 | 46 | 58 | 59 | 102 | 105 | 108 => charWidth(i) = 3
            case _ => charWidth(i) = 7
        }

    }


    def createStringGui(text: String, color: Color): Mesh = {
        val characters: Array[Char] = text.toCharArray
        val mm = MeshMaker
        mm.startMakeTriangles()
        mm.setTexture(ascii)
        mm.addColor(color)
        var startx: Float = 0
        val a: Float = 1f / 128f
        var b: Int = 0
        var u: Float = .0f
        var v: Float = .0f
        for (character <- characters) {

            b = charWidth(character)
            v = character.toInt / 16
            u = character.toInt - 16 * v
            mm.setCurrentIndex()
            mm.addVertexWithUV(startx, 8, ZPOS, u * 8 * a, v * 8 * a)
            mm.addVertexWithUV(startx, 0.0f, ZPOS, u * 8 * a, (v * 8 + 8) * a)
            mm.addVertexWithUV(startx + b, 0.0f, ZPOS, (u * 8 + b) * a, (v * 8 + 8) * a)
            mm.addVertexWithUV(startx + b, 8, ZPOS, (u * 8 + b) * a, v * 8 * a)
            mm.addIndex(0)
            mm.addIndex(1)
            mm.addIndex(2)
            mm.addIndex(3)
            mm.addIndex(0)
            mm.addIndex(2)
            startx += b
        }
        mm.makeMesh()
    }

}
