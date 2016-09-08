package ru.megains.engine.graph.renderer.gui

import org.lwjgl.input.{Keyboard, Mouse}
import ru.megains.game.OrangeCraft

import scala.collection.mutable



class GuiManager(val orangeCraft: OrangeCraft) {


    private val guiInGame:mutable.HashMap[String,GuiInGame] = mutable.HashMap[String,GuiInGame]()
    private var guiScreen:GuiScreen = _

    def init(): Unit ={
        addGuiInGame("hotBar",new GuiHotBar())
    }

    def isGuiScreen:Boolean = guiScreen != null



    def render(mouseX:Int,mouseY:Int): Unit ={
        if (guiScreen != null) {
            guiScreen.drawScreen(mouseX,mouseY)
        }else{
            guiInGame.values.filter(_!=null).foreach(_.drawScreen(mouseX,mouseY))
        }
    }

    def setGuiScreen(screen: GuiScreen) {
        if (guiScreen != null) {guiScreen.cleanup()}
        if (screen != null) {
            screen.init(orangeCraft)
            orangeCraft.ungrabMouseCursor()
        }else{
            orangeCraft.grabMouseCursor()
        }
        guiScreen = screen
    }


    def addGuiInGame(name:String,gui: GuiInGame): Unit ={
        if (gui != null) {gui.init(orangeCraft)}
        guiInGame += name -> gui
    }

    def removeGuiInGame(name:String): Unit = {
        guiInGame.remove(name)
    }
    def getGuiInGame(name: String):GuiInGame ={
        guiInGame.getOrElse(name,null)
    }

    def handleInput(): Unit ={
        while(Mouse.next()){

            val x = Mouse.getX
            val y = Mouse.getY
            val button = Mouse.getEventButton
            val buttonState = Mouse.getEventButtonState
            if(button == -1){
                guiScreen.mouseClickMove(x,y)
            }else if(buttonState){
                guiScreen.mouseClicked(x,y,button)
            }else{
                guiScreen.mouseReleased(x,y,button)
            }


        }


        while (Keyboard.next()) {
            if(Keyboard.getEventKeyState){
                guiScreen.keyTyped( Keyboard.getEventCharacter,Keyboard.getEventKey)
            }



        }
    }
}
