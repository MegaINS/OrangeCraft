package ru.megains.engine.graph.renderer.gui

import ru.megains.game.OrangeCraft

import scala.collection.mutable



class GuiManager(val orangeCraft: OrangeCraft) {


    private val guiInGame:mutable.HashMap[String,GuiInGame] = mutable.HashMap[String,GuiInGame]()
    private var guiScreen:GuiScreen = _

    def init(): Unit ={
        addGuiInGame("hotBar",new GuiHotBar(orangeCraft))
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
        if (screen != null) {screen.init()}
        guiScreen = screen
    }


    def addGuiInGame(name:String,gui: GuiInGame): Unit ={
        if (gui != null) {gui.init()}
        guiInGame += name -> gui
    }

    def removeGuiInGame(name:String): Unit = {
        guiInGame.remove(name)
    }
    def getGuiInGame(name: String):GuiInGame ={
        guiInGame.getOrElse(name,null)
    }
}
