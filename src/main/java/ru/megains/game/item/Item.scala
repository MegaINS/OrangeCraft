package ru.megains.game.item

import org.joml.Vector3f
import ru.megains.game.register.GameRegister


class Item(val name:String) {

}

object Item{

    val SCALE:Float = 40f
    val ROTATION = new Vector3f(-25,45,0)
    def getItemById(id:Int):Item = GameRegister.getItemById(id)

}
