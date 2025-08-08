package me.expandedcuber.hud

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.SlotActionType

class SkyMenuScreenHandler(
    type: ScreenHandlerType<*>,
    syncId: Int,
    playerInventory: PlayerInventory?,
    inventory: Inventory,
    rows: Int,
    private val factory: SkyScreenFactory
) : GenericContainerScreenHandler(type, syncId, playerInventory, inventory, rows) {
    override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType?, player: PlayerEntity?) {
        if(slotIndex in 0 until inventory.size()) factory.buttonClickEvents[slotIndex]?.invoke()
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }
}