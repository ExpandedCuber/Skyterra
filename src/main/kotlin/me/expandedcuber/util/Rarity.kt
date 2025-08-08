package me.expandedcuber.util

import me.expandedcuber.util.TextUtil
import net.minecraft.text.MutableText
import net.minecraft.text.TextColor

enum class Rarity {
    Common,
    Uncommon,
    Rare,
    Epic,
    Legendary;

    fun getColor(): TextColor {
        return when(this) {
            Common -> TextColor.fromRgb(0xFFFFFF)
            Uncommon -> TextColor.fromRgb(0x55FF55)
            Rare -> TextColor.fromRgb(0x5555FF)
            Epic -> TextColor.fromRgb(0xAA00AA)
            Legendary -> TextColor.fromRgb(0xFFAA00)
        }
    }

    fun getText(lower: Boolean): MutableText {
        val text = when(this) {
            Common -> "COMMON"
            Uncommon -> "UNCOMMON"
            Rare -> "RARE"
            Epic -> "EPIC"
            Legendary -> "LEGENDARY"
        }

        val colorHex = "#%06X".format(getColor().rgb and 0xFFFFFF)
        return TextUtil.literal("%$colorHex%" + if(lower) text.lowercase() else text)
    }
}