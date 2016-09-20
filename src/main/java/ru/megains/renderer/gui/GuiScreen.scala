package ru.megains.renderer.gui

import java.awt.Color

import org.lwjgl.input.Keyboard._
import ru.megains.game.OrangeCraft
import ru.megains.game.item.ItemStack
import ru.megains.renderer.mesh.Mesh
import ru.megains.renderer.{EntityRenderer, FontRender, RenderItem}

import scala.collection.mutable.ArrayBuffer

abstract class GuiScreen extends Gui {


    var background: Mesh = createGradientRect(800, 600, new Color(128, 128, 128, 128), new Color(0, 0, 0, 128))


    var itemRender: RenderItem = _
    var renderer: EntityRenderer = _
    var oc: OrangeCraft = _
    var fontRender: FontRender = _
    val buttonList: ArrayBuffer[GuiButton] = new ArrayBuffer[GuiButton]()

    def drawObject(mesh: Mesh, xPos: Int, yPos: Int): Unit = drawObject(xPos, yPos, 1, mesh, renderer)

    def drawObject(xPos: Int, yPos: Int, scale: Float, mesh: Mesh): Unit = {
        drawObject(xPos, yPos, scale, mesh, renderer)
    }

    def drawItemStack(itemStack: ItemStack, xPos: Int, yPos: Int): Unit = itemRender.renderItemStackToGui(xPos, yPos, itemStack)

    def mouseReleased(x: Int, y: Int, button: Int): Unit = {}

    def mouseClicked(x: Int, y: Int, button: Int): Unit = {
        if (button == 0) {
            buttonList.foreach(guiButton => {
                if (guiButton.isMouseOver(x, y)) {
                    actionPerformed(guiButton)
                }
            }
            )
        }

    }


    def mouseClickMove(x: Int, y: Int): Unit = {}


    def actionPerformed(button: GuiButton) {}

    def keyTyped(typedChar: Char, keyCode: Int) {
        keyCode match {
            case KEY_ESCAPE => oc.guiManager.setGuiScreen(null)
            case _ =>
        }
    }

    def init(orangeCraft: OrangeCraft): Unit = {
        oc = orangeCraft
        itemRender = oc.itemRender
        renderer = oc.renderer
        fontRender = oc.fontRender
    }

    def drawScreen(mouseX: Int, mouseY: Int): Unit = {
        buttonList.foreach(_.draw(mouseX, mouseY))
    }

    def drawDefaultBackground(): Unit = {
        drawObject(background, 0, 0)
    }

    def cleanup(): Unit = {
        buttonList.foreach(_.clear())
    }

    def createString(text: String, color: Color): Mesh = super.createString(fontRender, text, color)

}
