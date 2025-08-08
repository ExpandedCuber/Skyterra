package me.expandedcuber

import me.expandedcuber.item.ItemAbility
import me.expandedcuber.stats.Mana
import me.expandedcuber.stats.Stat
import me.expandedcuber.stats.PlayerStats
import net.minecraft.client.MinecraftClient
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object Scheduler {
    val abilityCooldowns = mutableMapOf<ItemAbility, Long>()
    val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    fun start() {
        scheduler.scheduleAtFixedRate({
            if(!MinecraftClient.getInstance().isPaused) {
                val now = System.currentTimeMillis()
                abilityCooldowns.entries.removeIf { it.value <= now }
            }
        }, 0, 100, TimeUnit.MILLISECONDS)

        scheduler.scheduleAtFixedRate({
            if(!MinecraftClient.getInstance().isPaused) {
                if(PlayerStats.get(Stat.MANA) < Stat.MANA.default) Mana.regenMana()
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS)
    }

    fun isOnCooldown(ability: ItemAbility): Boolean {
        val endTime = abilityCooldowns[ability]
        if(endTime == null) return false
        return endTime > System.currentTimeMillis()
    }

    fun startCooldown(ability: ItemAbility) {
        abilityCooldowns[ability] = System.currentTimeMillis() + ability.cooldown
    }

    fun getCooldown(ability: ItemAbility): Long {
        return abilityCooldowns[ability]?.minus(System.currentTimeMillis())?.coerceAtLeast(0) ?: 0
    }

    fun stop() {
        scheduler.shutdownNow()
    }
}