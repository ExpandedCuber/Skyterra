package me.expandedcuber.item.custom

import me.expandedcuber.SkyBobberDuck
import me.expandedcuber.item.SkyRod
import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.Vector

class GrapplingHook(settings: Settings) : SkyRod(settings) {
    init {
        this.rarity = Rarity.Uncommon
    }

    override fun getLore(): List<Text> {
        return listOf<Text>(
            TextUtil.literal("%no_italic%%gray%Travel around in style using this"),
            TextUtil.literal("%no_italic%%gray%Grappling Hook."),
            TextUtil.literal("%no_italic%%dark_gray%2 Second Cooldown"),
            Text.empty(),
            TextUtil.literal("%bold%%no_italic%%${this.rarity.getColor()}%"+ this.rarity.name.uppercase())
        )
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        val bobber = (user as SkyBobberDuck).`skyterra$getSkyBobber`()
        super.use(world, user, hand)

        if(bobber != null) {
            val bl = bobber.pos
            val pl = user.eyePos

            val velo = pl.subtract(bl).normalize().multiply(1.5)

            user.addVelocity(velo)

        }

        return ActionResult.SUCCESS
    }
}