package me.expandedcuber.config

import com.mojang.datafixers.util.Pair
import me.expandedcuber.Skyterra
import me.expandedcuber.hud.HudManager
import me.expandedcuber.hud.SkyHudElement
import me.expandedcuber.util.SimpleConfig
import org.joml.Vector2i
import java.io.PrintWriter

object Config {
    val configProvider = ConfigProvider()
    val config: SimpleConfig = SimpleConfig.of(Skyterra.MOD_ID + "-config").provider(configProvider).request()

    fun saveHudElements() {
        HudManager.getAllElements().forEach { element ->
            configProvider.addKeyValuePair(Pair.of(
                "key.skyterra.${element.id}",
                "${element.x},${element.y}"))
        }
    }

    fun getHudElementPosition(element: SkyHudElement): Vector2i? {
        val value = config.getOrDefault("key.skyterra.${element.id}", null)

        if(value == null) return null

        val pos = value.split(",")

        return Vector2i(pos[0].toInt(), pos[1].toInt())
    }

    fun register() {
        Skyterra.logger.info("Registering config.")
    }
}