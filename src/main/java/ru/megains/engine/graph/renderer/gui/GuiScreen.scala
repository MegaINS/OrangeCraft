package ru.megains.engine.graph.renderer.gui

import ru.megains.engine.graph.Renderer

abstract class GuiScreen extends Gui {

    def init(): Unit

    def render(renderer: Renderer): Unit

    def cleanup(): Unit

}
