package ru.megains.renderer.gui

import org.joml.Vector3f


class GuiRenderInfo(val position: Vector3f, val rotation: Vector3f, val scale: Float) {

    def this(xPos: Int, yPos: Int, xRot: Int, yRot: Int, zRot: Int, scale: Float) = {
        this(new Vector3f(xPos, yPos, 0), new Vector3f(xRot, yRot, zRot), scale)
    }

    def this(xPos: Int, yPos: Int) = {
        this(xPos, yPos, -25, 45, 0, 25)
    }


}
