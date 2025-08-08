package me.expandedcuber.components

import com.mojang.serialization.Codec
import me.expandedcuber.Skyterra
import me.expandedcuber.item.ItemType
import me.expandedcuber.stats.Stat
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModDataComponents {
    val ITEM_STAT: ComponentType<ItemStatComponent> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Skyterra.MOD_ID, "item_stat"),
        ComponentType.builder<ItemStatComponent>().codec(ItemStatComponent.CODEC).build()
    )

    val SLOT_CLICKABLE: ComponentType<Boolean> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Skyterra.MOD_ID, "slot_clickable"),
        ComponentType.builder<Boolean>().codec(Codec.BOOL).build()
    )

    val PLAYER_STATS: ComponentType<Map<Stat, Double>> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Skyterra.MOD_ID, "player_stats"),
        ComponentType.builder<Map<Stat, Double>>().codec(Codec.unboundedMap(ItemStatComponent.ITEM_STAT_CODEC, Codec.DOUBLE)).build()
    )

    val TOOL: ComponentType<ItemType> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Skyterra.MOD_ID, "tool_type"),
        ComponentType.builder<ItemType>().codec(ItemType.CODEC).build()
    )

    fun registerComponents() {
        Skyterra.logger.info("Data components registered.")
    }
}