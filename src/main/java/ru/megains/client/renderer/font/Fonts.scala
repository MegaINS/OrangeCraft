package ru.megains.client.renderer.font

import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.FontRender

object Fonts {
    val fontRender: FontRender = OrangeCraft.orangeCraft.fontRender
    val timesNewRomanR: Font = fontRender.loadFont("TimesNewRomanR")
}
