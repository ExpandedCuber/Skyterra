package me.expandedcuber.item

import me.expandedcuber.Skyterra
import me.expandedcuber.armor.custom.LeafletHelmet
import me.expandedcuber.components.ItemStatComponent
import me.expandedcuber.components.ModDataComponents
import me.expandedcuber.item.custom.AspectOfTheDragons
import me.expandedcuber.item.custom.AspectOfTheEnd
import me.expandedcuber.item.custom.AspectOfTheVoid
import me.expandedcuber.item.custom.GrapplingHook
import me.expandedcuber.item.custom.MenuItem
import me.expandedcuber.item.custom.Stonk
import me.expandedcuber.stats.Stat
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.component.type.EquippableComponent
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.ArmorMaterials
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundEvents
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import java.util.Optional

object ModItems {
    val ASPECT_OF_THE_END = register("aspect_of_the_end", ::AspectOfTheEnd) {
        maxCount(1)
        component(
            ModDataComponents.ITEM_STAT,
            ItemStatComponent(
                mapOf(
                    Stat.DAMAGE to 100.0
                )
            )
        )
        component(
            ModDataComponents.TOOL,
            ItemType.SWORD
        )
    }

    val ASPECT_OF_THE_VOID = register("aspect_of_the_void", ::AspectOfTheVoid) {
        maxCount(1)
        component(
            ModDataComponents.ITEM_STAT,
            ItemStatComponent(
                mapOf(
                    Stat.DAMAGE to 120.0,
                    Stat.STRENGTH to 100.0
                )
            )
        )
        component(
            ModDataComponents.TOOL,
            ItemType.SWORD
        )
    }

    val ASPECT_OF_THE_DRAGONS = register("aspect_of_the_dragons", ::AspectOfTheDragons) {
        maxCount(1)
    }
    val SKYBLOCK_MENU = register("skyblock_menu", ::MenuItem) {
        maxCount(1)
    }
    val GRAPPLING_HOOK = register("grappling_hook", ::GrapplingHook) {
        maxCount(1)
    }

    val LEAFLET_HELMET = registerArmor("leaflet_helmet", EquipmentType.HELMET, ArmorMaterials.LEATHER, ::LeafletHelmet) {
        component(DataComponentTypes.DYED_COLOR, DyedColorComponent(DyeColor.GREEN.entityColor))
    }

    val stonk = register("stonk", ::Stonk) {
        maxCount(1)
        component(
            ModDataComponents.ITEM_STAT,
            ItemStatComponent(
                mapOf(
                    Stat.MINING_SPEED to 510.0,
                    Stat.BREAKING_POWER to 1.0
                )
            )
        )
        component(
            ModDataComponents.TOOL,
            ItemType.PICKAXE
        )
        pickaxe()
    }

    inline fun <reified T : Item> register(
        name: String, crossinline factory: (Item.Settings) -> T, settingsBuilder: Item.Settings.() -> Unit = {}
    ): T {
        val settings = Item.Settings().apply(settingsBuilder)
        return Items.register(
            RegistryKey.of(
                RegistryKeys.ITEM, Identifier.of(Skyterra.MOD_ID, name)
            ), { factory(settings) }, settings
        ) as T
    }

    fun registerArmor(
        name: String,
        type: EquipmentType,
        material: ArmorMaterial,
        itemFactory: (Item.Settings) -> Item,
        settingsBuilder: Item.Settings.() -> Unit = {}
    ): Item {
        val settings = Item.Settings().apply(settingsBuilder)
            .component(
                DataComponentTypes.EQUIPPABLE,
                EquippableComponent(
                    type.equipmentSlot,
                    Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_SHEARS_SNIP),
                    Optional.of(material.assetId),
                    Optional.empty(),
                    Optional.empty(),
                    true,
                    true,
                    false,
                    true,
                    false,
                    Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_SHEARS_SNIP)
                )
            )

        return Items.register(
            RegistryKey.of(
                RegistryKeys.ITEM,
                Identifier.of(Skyterra.MOD_ID, name)
            ),
            { itemFactory(settings) },
            settings
        )
    }


    fun registerItems() {
        Skyterra.logger.info("Loaded mod items...")
    }
}