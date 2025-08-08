package me.expandedcuber.item.custom

import me.expandedcuber.hud.screens.SkyMenuScreen
import me.expandedcuber.item.SkyItem
import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class MenuItem(settings: Settings) : SkyItem(settings) {
    override fun getLore(): List<Text> {
        return mutableListOf(
            Text.empty(),
            TextUtil.literal("%no_italic%%gray%View all of your Skyblock"),
            TextUtil.literal("%no_italic%%gray%progress, including your Skills,"),
            TextUtil.literal("%no_italic%%gray%Collections, Recipes, and more!"),
            Text.empty(),
            TextUtil.literal("%no_italic%%yellow%Click to open!"),
        )
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        user.openHandledScreen(SkyMenuScreen())

        return ActionResult.SUCCESS
    }
}