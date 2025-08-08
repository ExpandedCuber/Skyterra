package me.expandedcuber

import me.expandedcuber.stats.Stat
import me.expandedcuber.stats.PlayerStats
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

object Damage {
    fun applyDamage(player: PlayerEntity, target: LivingEntity) {
        val baseDamage = 5 + PlayerStats.get(Stat.DAMAGE)
        val strengthMultiplier = 1 + PlayerStats.get(Stat.STRENGTH) / 100.0
        val additiveMultiplier = 1.0
        val multiplicativeMultiplier = 1.0

        val damageBeforeCrit = baseDamage * strengthMultiplier * additiveMultiplier * multiplicativeMultiplier

        val critMultiplier = 1 + (PlayerStats.get(Stat.CRIT_DAMAGE) / 100.0)

        var totalDamage = damageBeforeCrit

        if(Math.random() * 100 <= PlayerStats.get(Stat.CRIT_CHANCE)) {
            totalDamage *= critMultiplier
        }

        player.sendMessage(Text.literal("Dealt $totalDamage damage."), false)

        target.damage(player.world as ServerWorld, target.damageSources.playerAttack(player), totalDamage.toFloat())
    }
}