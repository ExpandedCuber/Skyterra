package me.expandedcuber.item.custom

import me.expandedcuber.item.SkyPick
import me.expandedcuber.util.Rarity

class Stonk(settings: Settings) : SkyPick(settings) {
    init {
        this.rarity = Rarity.Epic
    }
}