package me.expandedcuber.item

import com.mojang.serialization.Codec
import net.minecraft.util.StringIdentifiable

enum class ItemType : StringIdentifiable {
    HOE,
    PICKAXE,
    SWORD,
    AXE,
    SHOVEL;

    override fun asString(): String = name.lowercase()

    companion object {
        val CODEC = StringIdentifiable.createCodec(ItemType::values)
    }
}