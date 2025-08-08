package me.expandedcuber.item

import me.expandedcuber.SkyBobberDuck
import me.expandedcuber.entity.SkyBobberEntity
import me.expandedcuber.mixin.PlayerEntityMixin
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

open class SkyRod(settings: Settings) : SkyItem(settings) {
    init {
        this.itemType = "rod"
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        val itemStack = user.getStackInHand(hand)

        if((user as SkyBobberDuck).`skyterra$getSkyBobber`() != null) {
            val bobber = (user as SkyBobberDuck).`skyterra$getSkyBobber`()
            bobber.use(itemStack)
            return ActionResult.SUCCESS
        }

        if(user.fishHook != null) {
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH)
            user.fishHook?.use(itemStack)
        } else {
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START)
            world.spawnEntity(SkyBobberEntity(user, world))
        }

        return ActionResult.SUCCESS
    }
}