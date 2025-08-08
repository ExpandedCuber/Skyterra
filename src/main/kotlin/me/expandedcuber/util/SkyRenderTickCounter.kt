package me.expandedcuber.util

import net.minecraft.client.render.RenderTickCounter

class SkyRenderTickCounter(private val value: Float) : RenderTickCounter {
    override fun getDynamicDeltaTicks(): Float {
        return value
    }

    override fun getTickProgress(ignoreFreeze: Boolean): Float {
        return value
    }

    override fun getFixedDeltaTicks(): Float {
        return value
    }
}