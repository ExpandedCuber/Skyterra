package me.expandedcuber.item

import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class SkyItemDelegate(val item: Item) {
    var rarity = Rarity.Common
    var abilities = listOf<ItemAbility>().toMutableList()
    var itemType: String = "item"

    fun getCustomName(): Text {
        return TextUtil.literal("%${this.rarity.getColor()}%" + item.name.string)
    }

    fun getAbilityLore(): List<Text> {
        val lore = emptyList<Text>().toMutableList()
        val endLore = emptyList<Text>().toMutableList()

        this.abilities.forEach { ability ->
            if(ability.type == ItemAbilityType.PostHit || ability.type == ItemAbilityType.Basic) {
                endLore.addAll(ability.description)
                endLore.add(Text.empty())
            }

            val typeText: String = when(ability.type) {
                ItemAbilityType.LeftClick, ItemAbilityType.LeftClickBlock -> "LEFT CLICK"
                ItemAbilityType.RightClick, ItemAbilityType.RightClickBlock -> "RIGHT CLICK"
                ItemAbilityType.ShiftLeftClick -> "SNEAK LEFT CLICK"
                ItemAbilityType.ShiftRightClick -> "SNEAK RIGHT CLICK"
                else -> ""
            }

            lore.add(TextUtil.literal("%no_italic%%gold%Ability: ${ability.name} %reset%%yellow%%bold%$typeText"))
            lore.addAll(ability.description)
            lore.add(TextUtil.literal("%no_italic%%dark_gray%Mana Cost: %dark_aqua%" + ability.manaCost))
            lore.add(Text.empty())
        }

        lore.addAll(0, endLore)
        return lore
    }

    fun getLore(): List<Text> {
        val lore = emptyList<Text>().toMutableList()
        val abilityLore = this.getAbilityLore()

        lore.add(Text.empty())
        lore.addAll(abilityLore)

        val baseStyle = Style.EMPTY
            .withItalic(false)
            .withBold(true)
            .withColor(this.rarity.getColor())

        val rarityText = Text.literal(this.rarity.name.uppercase()).setStyle(baseStyle)
        val itemText = Text.literal(" " + this.itemType.uppercase()).setStyle(baseStyle)

        lore.add(rarityText.append(itemText))

        return lore
    }

    fun inventoryTick(stack: ItemStack, world: ServerWorld, entity: Entity, slot: EquipmentSlot?) {
        stack.get(DataComponentTypes.ITEM_NAME)?.equals(this.getCustomName())?.let {
            if(!it) {
                stack.set(DataComponentTypes.ITEM_NAME, this.getCustomName())
            }
        }

        stack.get(DataComponentTypes.LORE)?.equals(this.getLore())?.let {
            if(!it) {
                stack.set(DataComponentTypes.LORE, LoreComponent(this.getLore()))
            }
        }
    }

    fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        this.abilities.forEach { ability ->
            if(ability.type == ItemAbilityType.RightClick) {
                return ability.use(world, user, hand)
            } else if(ability.type == ItemAbilityType.ShiftRightClick && Screen.hasShiftDown()) {
                return ability.use(world, user, hand)
            }
        }

        return ActionResult.PASS
    }

    fun useOnBlock(context: ItemUsageContext): ActionResult {
        this.abilities.forEach { ability ->
            if(ability.type == ItemAbilityType.RightClickBlock) {
                return ability.use(context)
            }
        }

        return ActionResult.PASS
    }
}