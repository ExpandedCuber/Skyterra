package me.expandedcuber.stats

import me.expandedcuber.components.ItemStatComponent
import me.expandedcuber.components.ModDataComponents
import me.expandedcuber.util.TextUtil
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

enum class Stat(var default: Double) {
    MANA(500.0),
    MANA_REGEN(0.02),
    DAMAGE(0.0),
    STRENGTH(0.0),
    CRIT_CHANCE(30.0),
    MINING_SPEED(0.0),
    BREAKING_POWER(0.0),
    CRIT_DAMAGE(50.0);

    fun getLore(amount: Double): Text {
        return when (this) {
            MANA -> TextUtil.literal("%gray%Intelligence:%green%+$amount")
            MANA_REGEN -> TextUtil.literal("%gray%Mana Regen: %green%$amount")
            DAMAGE -> TextUtil.literal("%gray%Damage:%red%+$amount")
            STRENGTH -> TextUtil.literal("%gray%Strength:%red%+$amount")
            CRIT_CHANCE -> TextUtil.literal("%gray%Crit Chance:%red%+$amount%")
            CRIT_DAMAGE -> TextUtil.literal("%gray%Crit Damage:%red%+$amount%")
            MINING_SPEED -> TextUtil.literal("%gray%Mining Speed:%green%+$amount")
            BREAKING_POWER -> TextUtil.literal("%gray%Breaking Power $amount")
        }
    }
}

object PlayerStats {
    private val stats: MutableMap<Stat, StatValue> = Stat.entries.associateWith {
        StatValue(it.default, it.default)
    }.toMutableMap()

    fun get(stat: Stat): Double {
        return stats[stat]?.current ?: stat.default
    }

    fun set(stat: Stat, value: Double) {
        stats[stat]?.current = value
    }

    fun add(stat: Stat, amount: Double) {
        stats[stat]?.let { it.current += amount }
    }

    fun subtract(stat: Stat, amount: Double) {
        stats[stat]?.let { it.current -= amount }
    }

    fun addFromComponent(component: ItemStatComponent?): Boolean {
        if(component == null) return false

        component.stats.forEach { stat ->
            this.add(stat.key, stat.value)
        }

        return true
    }

    fun update(player: PlayerEntity) {
        reset()

        val stack = player.mainHandStack
        val itemStats = stack.get(ModDataComponents.ITEM_STAT)

        val helmet = player.getEquippedStack(EquipmentSlot.HEAD).get(ModDataComponents.ITEM_STAT)
        val chest = player.getEquippedStack(EquipmentSlot.CHEST).get(ModDataComponents.ITEM_STAT)
        val legs = player.getEquippedStack(EquipmentSlot.LEGS).get(ModDataComponents.ITEM_STAT)
        val boots = player.getEquippedStack(EquipmentSlot.FEET).get(ModDataComponents.ITEM_STAT)

        this.addFromComponent(itemStats)
        this.addFromComponent(helmet)
        this.addFromComponent(chest)
        this.addFromComponent(legs)
        this.addFromComponent(boots)

        player.setComponent(ModDataComponents.PLAYER_STATS, stats.mapValues { it.value.current })
    }

    fun reset() {
        stats.values.forEach { it.current = it.default }
    }
}