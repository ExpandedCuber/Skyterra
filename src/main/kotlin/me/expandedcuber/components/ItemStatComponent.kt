package me.expandedcuber.components

import com.mojang.serialization.Codec
import me.expandedcuber.stats.Stat
import net.minecraft.component.ComponentsAccess
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import java.util.function.Consumer

class ItemStatComponent(val stats: Map<Stat, Double>) : TooltipAppender {
    companion object {
        val ITEM_STAT_CODEC: Codec<Stat> = Codec.STRING.xmap(
            { name -> Stat.valueOf(name.uppercase()) },
            { stat -> stat.name.lowercase() }
        )
        val STATS_CODEC: Codec<Map<Stat, Double>> = Codec.unboundedMap(ITEM_STAT_CODEC, Codec.DOUBLE)
        val CODEC: Codec<ItemStatComponent> = STATS_CODEC.xmap(
            { map -> ItemStatComponent(map) },
            { component -> component.stats }
        )
    }

    override fun appendTooltip(
        context: Item.TooltipContext,
        textConsumer: Consumer<Text>,
        type: TooltipType,
        components: ComponentsAccess
    ) {
        stats.forEach { (stat, amount) ->
            textConsumer.accept(stat.getLore(amount))
        }
    }
}