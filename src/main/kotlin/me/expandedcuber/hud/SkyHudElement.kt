package me.expandedcuber.hud

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.MutableText
import net.minecraft.util.Colors
import kotlin.math.roundToInt

abstract class SkyHudElement {
    abstract var id: String
    abstract var x: Int
    abstract var y: Int
    abstract var enabled: Boolean
    abstract var width: Int
    abstract var height: Int
    abstract var text: MutableText
    open var scale: Float = 1.0f
    var initialized = false

    abstract fun setDefaultPos(context: DrawContext)

    companion object {
        fun renderElement(element: SkyHudElement, context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean) {
            val client = MinecraftClient.getInstance()
            element.width = client.textRenderer.getWidth(element.text)
            element.height = client.textRenderer.fontHeight

            context.matrices.pushMatrix()
            context.matrices.scale(element.scale, element.scale)

            element.render(context, tickCounter, inEditor)

            if(inEditor) {
                context.drawBorder(element.x - 1, element.y - 1, element.width, element.height, Colors.GRAY)
            }

            context.matrices.popMatrix()
        }
    }

    abstract fun render(context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean)

    fun renderInEditor(context: DrawContext, tickCounter: RenderTickCounter) {
        render(context, tickCounter, true)
    }
}