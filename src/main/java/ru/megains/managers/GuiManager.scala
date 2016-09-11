package ru.megains.managers

import org.lwjgl.input.{Keyboard, Mouse}
import ru.megains.game.OrangeCraft
import ru.megains.renderer.gui._

import scala.collection.mutable


class GuiManager(val orangeCraft: OrangeCraft) {


    private val guiInGame: mutable.HashMap[String, GuiInGame] = mutable.HashMap[String, GuiInGame]()
    private var guiScreen: GuiScreen = _

    def init(): Unit = {
        addGuiInGame("hotBar", new GuiHotBar)
        addGuiInGame("debugInfo", new GuiDebugInfo)
        addGuiInGame("blockSelect", new GuiBlockSelect)
    }

    def tick(): Unit = {
        guiInGame.values.filter(_ ne null).foreach(_.tick())
    }

    def addGuiInGame(name: String, gui: GuiInGame): Unit = {
        if (gui ne null) {
            gui.init(orangeCraft)
        }
        guiInGame += name -> gui
    }

    def isGuiScreen: Boolean = guiScreen ne null

    def setGuiScreen(screen: GuiScreen) {
        if (guiScreen ne null) {
            guiScreen.cleanup()
        }

        if (screen ne null) {
            screen.init(orangeCraft)
            orangeCraft.ungrabMouseCursor()
        } else {
            orangeCraft.grabMouseCursor()
        }
        guiScreen = screen
    }

    def draw(mouseX: Int, mouseY: Int): Unit = {
        if (guiScreen ne null) {
            guiScreen.drawScreen(mouseX, mouseY)
        } else {
            guiInGame.values.filter(_ ne null).foreach(_.drawScreen(mouseX, mouseY))
        }
    }

    def removeGuiInGame(name: String): Unit = {
        guiInGame.remove(name)
    }

    def getGuiInGame(name: String): GuiInGame = {
        guiInGame.getOrElse(name, null)
    }

    def handleInput(): Unit = {
        while (Mouse.next()) {

            val x = Mouse.getX
            val y = Mouse.getY
            val button = Mouse.getEventButton
            val buttonState = Mouse.getEventButtonState
            if (button == -1) {
                guiScreen.mouseClickMove(x, y)
            } else if (buttonState) {
                guiScreen.mouseClicked(x, y, button)
            } else {
                guiScreen.mouseReleased(x, y, button)
            }


        }


        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState) {
                guiScreen.keyTyped(Keyboard.getEventCharacter, Keyboard.getEventKey)
            }


        }
    }
}
