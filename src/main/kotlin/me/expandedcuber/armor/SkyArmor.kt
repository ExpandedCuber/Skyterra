package me.expandedcuber.armor

import me.expandedcuber.item.SkyItem
import net.minecraft.item.equipment.EquipmentType

open class SkyArmor(settings: Settings) : SkyItem(settings) {
    open var equipmentType: EquipmentType = EquipmentType.HELMET
}