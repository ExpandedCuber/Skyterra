package me.expandedcuber.hud.screens

import me.expandedcuber.hud.SkyMenuScreenHandler
import me.expandedcuber.hud.SkyScreenFactory
import me.expandedcuber.util.TextUtil
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class SkyMenuScreen
    : SkyScreenFactory() {
    override fun getDisplayName(): Text? {
        return TextUtil.literal("%bold%%white%Skyblock Menu")
    }

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory?,
        player: PlayerEntity?
    ): ScreenHandler? {
        val inv = SimpleInventory(54)

        this.createSlot(inv, 13, TextUtil.literal("Player"), item = Items.STICK)
        this.createSlot(inv, 19, TextUtil.literal("Skill menu!"), item = Items.DIAMOND_SWORD)
        this.createSlot(inv, 20, TextUtil.literal("Collections"), item = Items.PAINTING)
        this.createSlot(inv, 21, TextUtil.literal("Recipes"), item = Items.BOOK)
        this.createSlot(inv, 22, TextUtil.literal("Skyblock Level"), item = Items.STICK)
        this.createSlot(inv, 23, TextUtil.literal("Quests"), item = Items.WRITABLE_BOOK)
        this.createSlot(inv, 24, TextUtil.literal("Calendar"), item = Items.CLOCK)
        this.createSlot(inv, 25, TextUtil.literal("Storage"), item = Items.CHEST)
        this.createSlot(inv, 29, TextUtil.literal("Sack of sacks"), item = Items.STICK)
        this.createSlot(inv, 30, TextUtil.literal("Pets"), item = Items.BONE)
        this.createSlot(inv, 31, TextUtil.literal("Crafting"), item = Items.CRAFTING_TABLE)

        val chestplate = ItemStack(Items.LEATHER_CHESTPLATE)
        chestplate.set(DataComponentTypes.DYED_COLOR, DyedColorComponent(DyeColor.PURPLE.entityColor))

        this.createSlot(inv, 32, TextUtil.literal("Wardrobe"), stack = chestplate)
        this.createSlot(inv, 33, TextUtil.literal("Bank"), item = Items.STICK)
        this.createSlot(inv, 47, TextUtil.literal("Warp Menu"), item = Items.STICK)
        this.createSlot(inv, 48, TextUtil.literal("Profile Switcher"), item = Items.NAME_TAG)
        this.createSlot(inv, 49, TextUtil.literal("Close"), item = Items.BARRIER)
        this.createSlot(inv, 50, TextUtil.literal("Settings"), item = Items.REDSTONE_TORCH)
        this.createSlot(inv, 51, TextUtil.literal("Booster Cookie"), item = Items.COOKIE)

        for(i in 0 until inv.size()) {
            if(inv.getStack(i) == ItemStack.EMPTY) {
                inv.setStack(i, ItemStack(Items.GRAY_STAINED_GLASS_PANE))
            }
        }

        return SkyMenuScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inv, 6, this)
    }
}