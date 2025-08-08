package me.expandedcuber.util

import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Colors
import net.minecraft.util.Formatting

object TextUtil {
    @JvmStatic
    fun literal(content: String): MutableText {
        val regex = Regex("%([a-zA-Z0-9_#]+)%")

        val matches = regex.findAll(content).toList()
        var lastIndex = 0
        var currentStyle = Style.EMPTY

        var result: MutableText? = null

        matches.forEach { match ->
            if (match.range.first > lastIndex) {
                val plainText = content.substring(lastIndex, match.range.first)
                val component = Text.literal(plainText).setStyle(currentStyle)
                if (result == null) {
                    result = component
                } else {
                    result.append(component)
                }
            }

            val tag = match.groupValues[1].lowercase()
            currentStyle = applyTag(currentStyle, tag)

            lastIndex = match.range.last + 1
        }

        if (lastIndex < content.length) {
            val plainText = content.substring(lastIndex)
            val component = Text.literal(plainText).setStyle(currentStyle)
            if (result == null) {
                result = component
            } else {
                result.append(component)
            }
        }

        return result ?: Text.literal("")
    }

    fun applyTag(currentStyle: Style, tag: String): Style {
        return when(tag) {
            "black" -> currentStyle.withColor(Formatting.BLACK)
            "bold" -> currentStyle.withBold(true)
            "italic" -> currentStyle.withItalic(true)
            "dark_gray" -> currentStyle.withColor(Formatting.DARK_GRAY)
            "gray" -> currentStyle.withColor(Formatting.GRAY)
            "blue" -> currentStyle.withColor(Colors.BLUE)
            "no_italic" -> currentStyle.withItalic(false)
            "reset" -> Style.EMPTY
            "gold" -> currentStyle.withColor(Formatting.GOLD)
            "yellow" -> currentStyle.withColor(Formatting.YELLOW)
            "white" -> currentStyle.withColor(Formatting.WHITE)
            "green" -> currentStyle.withColor(Formatting.GREEN)
            "dark_aqua" -> currentStyle.withColor(Formatting.DARK_AQUA)
            "red" -> currentStyle.withColor(Formatting.RED)
            "aqua" -> currentStyle.withColor(Formatting.AQUA)
            else -> {
                return if (tag.startsWith("#")) {
                    val hexString = tag.split("#")[1]
                    val colorInt = TextColor.fromRgb(hexString.toInt(16))
                    currentStyle.withColor(colorInt)
                } else {
                    currentStyle
                }
            }
        }
    }
}