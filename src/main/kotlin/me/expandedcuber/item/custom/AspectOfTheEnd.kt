package me.expandedcuber.item.custom

import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import me.expandedcuber.item.ItemAbility
import me.expandedcuber.item.ItemAbilityType
import me.expandedcuber.item.SkySword
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

class AspectOfTheEnd(settings: Settings) : SkySword(settings) {
    class TeleportAbility(item: Item, type: ItemAbilityType)
        : ItemAbility(item, "Instant Transmission",
            listOf(
                TextUtil.literal("%no_italic%%gray%Teleports you %green%8 %gray%blocks ahead of you and"),
                TextUtil.literal("%no_italic%%gray%gain %green%+50 %gray%Speed for %green%3 %gray%seconds.")
            ),
            50, 0, type) {

        override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
            if(world.isClient) return super.use(world, player, hand)

            val eyePos = player.getCameraPosVec(1.0f)
            val lookDir = player.getRotationVec(1.0f)
            val targetPos = eyePos.add(lookDir.multiply(7.0))

            val hit = player.world.raycast(
                RaycastContext(
                    eyePos,
                    targetPos,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
                )
            )

            when (hit.type) {
                HitResult.Type.BLOCK -> {
                    val pos = hit.blockPos.up()

                    player.requestTeleport(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)

                    player.setVelocity(0.0, 0.0, 0.0)
                    player.velocityModified = true

                    return ActionResult.PASS
                }
                HitResult.Type.MISS, HitResult.Type.ENTITY -> {
                    val pos = hit.pos

                    player.requestTeleport(pos.x, pos.y, pos.z)

                    player.setVelocity(0.0, 0.0, 0.0)
                    player.velocityModified = true

                    player.fallDistance = 0.0

                    return ActionResult.PASS
                }
                else -> {
                }
            }

            return ActionResult.PASS
        }
    }

    init {
        this.rarity = Rarity.Rare
        this.abilities.add(TeleportAbility(this, ItemAbilityType.RightClick))
    }
}