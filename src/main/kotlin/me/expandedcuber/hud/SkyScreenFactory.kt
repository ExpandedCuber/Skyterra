package me.expandedcuber.hud

import me.expandedcuber.components.ModDataComponents
import me.expandedcuber.util.TextUtil
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.MutableText
import net.minecraft.text.Text

open class SkyScreenFactory : NamedScreenHandlerFactory {
    var buttonClickEvents = emptyMap<Int, () -> Unit>().toMutableMap()

    override fun getDisplayName(): Text? {
        return TextUtil.literal("%gray%Untitled menu")
    }

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory?,
        player: PlayerEntity?
    ): ScreenHandler? {
        return GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, SimpleInventory(54), 54)
    }

    fun createSlot(inventory: SimpleInventory, slot: Int, name: Text, lore: List<MutableText> = emptyList(),  stack: ItemStack? = ItemStack.EMPTY, item: Item? = stack?.item, onClick: () -> Unit = {}) {
        var itemStack = stack

        if(itemStack?.isEmpty ?: true && item != null) {
            itemStack = ItemStack(item)
        }

        itemStack?.set(DataComponentTypes.LORE, LoreComponent(lore))
        itemStack?.set(ModDataComponents.SLOT_CLICKABLE, true)
        itemStack?.set(DataComponentTypes.ITEM_NAME, name)

        inventory.setStack(slot, itemStack)
        buttonClickEvents[slot] = onClick
    }
}