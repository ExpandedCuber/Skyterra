package me.expandedcuber.hud

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement

object HudManager {
    private val elements = mutableListOf<SkyHudElement>()

    fun register(element: SkyHudElement) {
        elements.add(element)
    }

    fun getEnabledElements(): List<SkyHudElement> = elements.filter { it.enabled }
    fun getAllElements(): List<SkyHudElement> = elements

    fun loadFromConfig() {

    }

    fun saveToConfig() {

    }
}