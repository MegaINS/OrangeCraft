package ru.megains.client.renderer.gui

import java.awt.Color

import org.joml.Vector3f
import org.lwjgl.opengl.Display
import ru.megains.client.OrangeCraft
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.item.ItemStack
import ru.megains.common.util.RayTraceResult

class GuiBlockSelect extends GuiInGame with GuiText {

    var ray: RayTraceResult = _
    var blockSelect: Block = _

    override def initGui(orangeCraft: OrangeCraft): Unit = {


        addText("Block.name", createString("", Color.WHITE))
        addText("Block.side", createString("", Color.WHITE))
        addText("Block.x", createString("", Color.WHITE))
        addText("Block.y", createString("", Color.WHITE))
        addText("Block.z", createString("", Color.WHITE))
        addText("Block.hp", createString("", Color.WHITE))
    }

    override def tick(): Unit = {
        if (oc.objectMouseOver ne ray) {
            ray = oc.objectMouseOver
            if (ray ne null) {
                val posB: BlockPos = ray.getBlockWorldPos
                val vec: Vector3f = ray.hitVec
                val hp = oc.world.getBlockHp(posB).toString
                blockSelect = ray.block
                addText("Block.name", createString(ray.block.name, Color.WHITE))
                addText("Block.x", createString("x: " + posB.worldX + "  " + vec.x, Color.WHITE))
                addText("Block.y", createString("y: " + posB.worldY + "  " + vec.y, Color.WHITE))
                addText("Block.z", createString("z: " + posB.worldZ + "  " + vec.z, Color.WHITE))
                addText("Block.side", createString("side: " + ray.sideHit.name, Color.WHITE))
                addText("Block.hp", createString("HP: " + hp, Color.WHITE))
            }
        }
    }

    override def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        if (ray ne null) {
            val weight = Display.getWidth / 2 - 100
            drawObject(weight, Display.getHeight - 20, 2, text("Block.name"))
            drawObject(weight, Display.getHeight - 40, 2, text("Block.x"))
            drawObject(weight, Display.getHeight - 60, 2, text("Block.y"))
            drawObject(weight, Display.getHeight - 80, 2, text("Block.z"))
            drawObject(weight, Display.getHeight - 100, 2, text("Block.side"))
            drawObject(weight, Display.getHeight - 100, 2, text("Block.side"))
            drawObject(weight, Display.getHeight - 120, 2, text("Block.hp"))
            drawItemStack(new ItemStack(blockSelect), Display.getWidth / 2 - 150, Display.getHeight - 50)
        }

    }

}
