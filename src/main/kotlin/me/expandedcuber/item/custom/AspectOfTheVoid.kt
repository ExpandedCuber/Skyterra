package me.expandedcuber.item.custom

import me.expandedcuber.item.ItemAbility
import me.expandedcuber.item.ItemAbilityType
import me.expandedcuber.item.SkyItem
import me.expandedcuber.item.SkySword
import me.expandedcuber.util.PlayerUtil
import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

class AspectOfTheVoid(settings: Settings) : SkySword(settings) {
    class InstantTransmission(item: Item, type: ItemAbilityType)
        : ItemAbility(item, "Instant Transmission",
        listOf(
            TextUtil.literal("%no_italic%%gray%Teleports you %green%8 %gray%blocks ahead of you and"),
            TextUtil.literal("%no_italic%%gray%gain %green%+50 %gray%Speed for %green%3 %gray%seconds.")
        ),
        45, 0, type) {

        override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
            if(world.isClient) {
                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT)
                return ActionResult.SUCCESS
            }


            return PlayerUtil.raycastTeleport(player, 8.0)
        }
    }

    class EtherTransmission(item: Item, type: ItemAbilityType)
        : ItemAbility(item, "Ether Transmission",
            listOf(
                TextUtil.literal("%no_italic%%gray%Teleport to your targeted block up"),
                TextUtil.literal("%no_italic%%gray%to %green%57 blocks%gray% away.")
            ),
            180, 0, type) {

            override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
                if(world.isClient) {
                    player.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT)
                    return ActionResult.SUCCESS
                }

                val pos = PlayerUtil.raycastBlock(player, 57.0)

                if(pos != null) {
                    player.requestTeleport(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5)

                    return ActionResult.SUCCESS
                } else {
                    return ActionResult.PASS
                }

            }
        }

    init {
        this.rarity = Rarity.Epic
        this.abilities = listOf(
            InstantTransmission(this, ItemAbilityType.RightClick),
            EtherTransmission(this, ItemAbilityType.ShiftRightClick)
        ).toMutableList()
    }
}