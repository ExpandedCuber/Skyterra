package me.expandedcuber.datagen

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(p0: BlockStateModelGenerator?) {
    }

    override fun generateItemModels(generator: ItemModelGenerator?) {
    }
}