package me.expandedcuber.armor

import net.minecraft.item.equipment.EquipmentType

open class SkyHelmet(settings: Settings) : SkyArmor(settings) {
    init {
        this.itemType = "helmet"
        this.equipmentType = EquipmentType.HELMET
    }
}