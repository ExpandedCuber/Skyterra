package me.expandedcuber.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

abstract class ItemAbility(val item: Item, val name: String, val description: List<Text>, val manaCost: Int, val cooldown: Long, val type: ItemAbilityType) {
    open fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult { return ActionResult.PASS }
    open fun use(context: ItemUsageContext): ActionResult { return ActionResult.PASS }
    open fun use(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult { return ActionResult.PASS }
    open fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) { return }
}