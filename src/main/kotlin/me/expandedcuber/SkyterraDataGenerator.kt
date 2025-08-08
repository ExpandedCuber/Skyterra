package me.expandedcuber

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object SkyterraDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		val pack = generator.createPack()
	}
}