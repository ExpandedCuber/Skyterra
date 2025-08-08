package me.expandedcuber.util

import me.expandedcuber.Skyterra
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


object ModTags {
    object Blocks {
        val NEEDS_BREAKING_POWER_1 = createTag("needs_breaking_power_1")
        val NEEDS_BREAKING_POWER_2 = createTag("needs_breaking_power_2")
        val NEEDS_BREAKING_POWER_3 = createTag("needs_breaking_power_3")
        val NEEDS_BREAKING_POWER_4 = createTag("needs_breaking_power_4")
        val NEEDS_BREAKING_POWER_5 = createTag("needs_breaking_power_5")
        val NEEDS_BREAKING_POWER_6 = createTag("needs_breaking_power_6")
        val NEEDS_BREAKING_POWER_7 = createTag("needs_breaking_power_7")
        val NEEDS_BREAKING_POWER_8 = createTag("needs_breaking_power_9")
        val NEEDS_BREAKING_POWER_9 = createTag("needs_breaking_power_1")

        private fun createTag(name: String): TagKey<Block> {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Skyterra.MOD_ID, name))
        }
    }
}