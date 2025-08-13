package me.expandedcuber

import me.expandedcuber.components.ModDataComponents
import me.expandedcuber.config.Config
import me.expandedcuber.hud.GuiConfigScreen
import me.expandedcuber.hud.HudManager
import me.expandedcuber.hud.ManaElement
import me.expandedcuber.item.ModItems
import me.expandedcuber.stats.PlayerStats
import me.expandedcuber.world.chunk.VoidChunkGenerator
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.render.DimensionEffects
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.biome.source.BiomeSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Skyterra : ModInitializer {
	const val MOD_ID = "skyterra"
	val logger: Logger = LoggerFactory.getLogger(MOD_ID)
	private var previousSlot = -1
	val openConfigButton: KeyBinding = KeyBindingHelper.registerKeyBinding(
		KeyBinding("key.skyterra.open_config", InputUtil.GLFW_KEY_RIGHT_SHIFT, "category.skyterra.keys")
	)

	val DRAGON_RAGE_DAMAGE: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID, "dragon_rage"))

	override fun onInitialize() {
		logger.info("Skyterra has started.")

		Config.register()
		ModItems.registerItems()
		ModDataComponents.registerComponents()

		Scheduler.start()

		HudManager.register(ManaElement())

		Registry.register(
			Registries.CHUNK_GENERATOR,
			Identifier.of(MOD_ID, "void"),
			VoidChunkGenerator.CODEC
		)

		ClientTickEvents.END_CLIENT_TICK.register { client ->
			val player = client.player ?: return@register

			val currentSlot = player.inventory.selectedSlot
			if(currentSlot != previousSlot) {
				previousSlot = currentSlot

				PlayerStats.update(player)
			}

			if(openConfigButton.wasPressed()) {
				println("Opening config screen")
				MinecraftClient.getInstance().setScreen(GuiConfigScreen())
			}
		}

		ClientLifecycleEvents.CLIENT_STOPPING.register {
			Scheduler.stop()
			Config.saveHudElements()
			Config.config.save()
		}
	}

}