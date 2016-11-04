package ru.megains.common.entity.item

import ru.megains.common.entity.Entity
import ru.megains.common.item.Item
import ru.megains.common.world.World

class EntityItem(world: World, val item: Item) extends Entity(world, 0.25f, 0.25f, 0.25f) {


    override def update(): Unit = {
        motionY -= 0.01f

        moveFlying(0, 0, if (onGround) 0.03f else 0.01f)
        move(motionX, motionY, motionZ)
    }
}
