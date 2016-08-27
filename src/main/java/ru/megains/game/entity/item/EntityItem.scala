package ru.megains.game.entity.item

import ru.megains.game.entity.Entity
import ru.megains.game.item.Item

class EntityItem(val item:Item) extends Entity( 0.25f , 0.25f , 0.25f){



  override def update(): Unit = {
    motionY -= 0.01f

    moveFlying(0, 0, if (onGround) 0.03f else 0.01f)
    move(motionX, motionY, motionZ)
  }
}