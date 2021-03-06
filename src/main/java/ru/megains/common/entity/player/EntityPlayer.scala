package ru.megains.common.entity.player


import ru.megains.common.block.blockdata.BlockDirection
import ru.megains.common.entity.EntityLivingBase
import ru.megains.common.inventory.{Container, ContainerPlayerInventory}
import ru.megains.common.item.ItemStack
import ru.megains.common.register.{Blocks, Items}
import ru.megains.common.world.World
import ru.megains.nbt.tag.NBTCompound


class EntityPlayer(val name: String, world: World) extends EntityLivingBase(world, 1.8f, 0.6f, 1.6f) {


    var openContainer: Container = _
    val inventory = new InventoryPlayer(this)
    val inventoryContainer: Container = new ContainerPlayerInventory(inventory)
    openContainer = inventoryContainer





    setPosition(0, 35, 0)


    def turn(xo: Double, yo: Double) {
        rotationYaw += xo.toFloat * 0.15f
        rotationPitch -= yo.toFloat * 0.15f
        if (rotationPitch < -90.0F) {
            rotationPitch = -90.0F
        }
        if (rotationPitch > 90.0F) {
            rotationPitch = 90.0F
        }
        if (rotationYaw > 360.0F) {
            rotationYaw -= 360.0F

        }
        if (rotationYaw < 0.0F) {
            rotationYaw += 360.0F

        }
    }

    def update(xo: Float, yo: Float, zo: Float) {


        // lastTickPosition.set(position)
        //   prevPosition.set(position)
        if (yo > 0) {
        }
        //  ydRot = yRot
        //xdRot = xRot
        // rotation.set(xRot, yRot, 0)
        motionY = yo / 2
        moveFlying(xo, zo, if (onGround) 0.04f else 0.02f)
        move(motionX, motionY, motionZ)
        motionX *= 0.8f
        if (motionY > 0.0f) {
            motionY *= 0.90f
        }
        else {
            motionY *= 0.98f
        }
        motionZ *= 0.8f
        motionY -= 0.04f
        if (onGround) {
            motionX *= 0.9f
            motionZ *= 0.9f
        }
    }

    override def update(): Unit = {
        rotationYaw match {
            case y if y > 315 || y < 45 => side = BlockDirection.NORTH
            case y if y < 135 => side = BlockDirection.EAST
            case y if y < 225 => side = BlockDirection.SOUTH
            case y if y < 315 => side = BlockDirection.WEST
            case _ => side = BlockDirection.UP
        }
    }

    override def getItemStackFromSlot: ItemStack = inventory.getStackSelect

    def setItemStackToSlot(stack: ItemStack) {

        // playEquipSound(stack)
        inventory.mainInventory(inventory.stackSelect) = stack


    }

    override def readEntityFromNBT(compound: NBTCompound): Unit = {

        // inventory.readFromNBT(compound)

        inventory.addItemStackToInventory(new ItemStack(Blocks.micro1, 12))
        inventory.addItemStackToInventory(new ItemStack(Blocks.micro2, 14))
        //  inventory.addItemStackToInventory(new ItemStack(Blocks.micro3))
        //inventory.addItemStackToInventory(new ItemStack(Blocks.micro4))
        inventory.addItemStackToInventory(new ItemStack(Items.stick, 12))
    }

    override def writeEntityToNBT(compound: NBTCompound): Unit = {
        inventory.writeToNBT(compound)
    }

}
