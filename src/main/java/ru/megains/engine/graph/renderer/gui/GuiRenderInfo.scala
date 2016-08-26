package ru.megains.engine.graph.renderer.gui

import org.joml.Vector3f
import ru.megains.game.item.Item


class GuiRenderInfo(val position:Vector3f,val rotation:Vector3f,val scale:Float) {

    def this(xPos:Int,yPos:Int,xRot:Int,yRot:Int,zRot:Int,scale:Float) = {
        this(new Vector3f(xPos,yPos,0),new Vector3f(xRot,yRot,zRot),scale)
    }
    def this(xPos:Int,yPos:Int) = {
        this(xPos,yPos,-25,45,0,Item.SCALE)
    }


}
