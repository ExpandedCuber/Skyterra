package me.expandedcuber.stats

import me.expandedcuber.Scheduler
import me.expandedcuber.item.ItemAbility
import me.expandedcuber.util.AbilityResult
import me.expandedcuber.util.TextUtil
import net.minecraft.entity.player.PlayerEntity

object Mana {
    fun useAbility(player: PlayerEntity, ability: ItemAbility): AbilityResult {
        val mana = PlayerStats.get(Stat.MANA)

        if(mana < ability.manaCost) {
            player.sendMessage(
                TextUtil.literal("%red%Not enough mana."),
                false
            )

            return AbilityResult.NoMana
        }

        if(Scheduler.isOnCooldown(ability)) {
            player.sendMessage(
                TextUtil.literal("This ability is on cooldown for ${Scheduler.getCooldown(ability)/1000} seconds."),
                false
            )

            return AbilityResult.OnCooldown
        }

        PlayerStats.set(Stat.MANA, mana - ability.manaCost)
        player.sendMessage(
            TextUtil.literal("%dark_aqua%-${ability.manaCost} mana: %blue%${ability.name}"),
            true
        )
        Scheduler.startCooldown(ability)

        return AbilityResult.Success
    }

    fun hasEnoughMana(player: PlayerEntity, ability: ItemAbility): Boolean {
        val mana = PlayerStats.get(Stat.MANA)
        return mana >= ability.manaCost
    }

    fun useMana(player: PlayerEntity, ability: ItemAbility): AbilityResult {
        val mana = PlayerStats.get(Stat.MANA)

        if(mana < ability.manaCost) {
            player.sendMessage(
                TextUtil.literal("%red%Not enough mana."),
                false
            )
            return AbilityResult.NoMana
        }

        if(Scheduler.isOnCooldown(ability)) {
            player.sendMessage(
                TextUtil.literal("This ability is on cooldown for ${Scheduler.getCooldown(ability)/1000} seconds."),
                false
            )
            return AbilityResult.OnCooldown
        }

        PlayerStats.set(Stat.MANA, mana - ability.manaCost)
        player.sendMessage(
            TextUtil.literal("%dark_aqua%-${ability.manaCost} mana: %blue%${ability.name}"),
            true
        )
        Scheduler.startCooldown(ability)
        return AbilityResult.Success
    }


    fun regenMana() {
        val mana = PlayerStats.get(Stat.MANA)
        val baseMana = Stat.MANA.default
        val regen = PlayerStats.get(Stat.MANA_REGEN)
        val amount = baseMana * regen

        if(mana + amount > baseMana) {
            PlayerStats.set(Stat.MANA, amount - baseMana)
            return
        }

        PlayerStats.add(Stat.MANA, baseMana * regen)
    }
}