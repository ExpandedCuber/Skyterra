package me.expandedcuber.item

import me.expandedcuber.Damage
import me.expandedcuber.stats.Mana
import me.expandedcuber.util.Rarity
import me.expandedcuber.util.TextUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

open class SkyItem(settings: Settings) : Item(settings) {
    var rarity = Rarity.Common
    var abilities = listOf<ItemAbility>().toMutableList()
    var itemType: String = "item"

    open fun getCustomName(): MutableText {
        return Text.literal(this.name.string).withColor(this.rarity.getColor().rgb)
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

    open fun getLore(): List<Text> {
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

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        if(world.isClient) return super.use(world, user, hand)

        val isShift = Screen.hasShiftDown()

        println(isShift)
        if (isShift) {
            val shiftAbility = abilities.firstOrNull { it.type == ItemAbilityType.ShiftRightClick }
            if (shiftAbility != null) {
                if(Mana.hasEnoughMana(user, shiftAbility)) {
                    val res = shiftAbility.use(world, user, hand)

                    if(res == ActionResult.SUCCESS) {
                        Mana.useMana(user, shiftAbility)
                    }

                    return res
                }
            }
        }

        val rightClickAbility = abilities.firstOrNull { it.type == ItemAbilityType.RightClick }
        if (rightClickAbility != null) {
            if(Mana.hasEnoughMana(user, rightClickAbility)) {
                val res = rightClickAbility.use(world, user, hand)

                if(res == ActionResult.SUCCESS) {
                    Mana.useMana(user, rightClickAbility)
                }

                return res
            }

        }

        return ActionResult.PASS
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if(context.world.isClient) return ActionResult.PASS

        this.abilities.forEach { ability ->
            if(ability.type == ItemAbilityType.RightClickBlock) {
                return ability.use(context)
            }
        }

        return ActionResult.PASS
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if(attacker.world.isClient) return

        this.abilities.forEach { ability ->
            if(ability.type == ItemAbilityType.PostHit) {
                ability.postHit(stack, target, attacker)
            }
        }

        if(attacker is PlayerEntity) Damage.applyDamage(attacker, target)
    }
}