package me.expandedcuber.hud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.MutableText
import net.minecraft.util.Colors

abstract class SkyHudElement {
    abstract var id: String
    abstract var x: Int
    abstract var y: Int
    abstract var enabled: Boolean
    abstract var width: Int
    abstract var height: Int
    abstract var scaledWidth: Int
    abstract var scaledHeight: Int
    abstract var scaledX: Int
    abstract var scaledY: Int
    abstract var text: MutableText
    open var scale: Float = 1.0f
    var initialized = false

    var anchor: Anchor = Anchor.TOP_LEFT
    var offsetX: Int = 0
    var offsetY: Int = 0

    abstract fun setDefaultPos()

    fun calculatePosition(screenWidth: Int, screenHeight: Int) {
        val baseX = when (anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT -> 0
            Anchor.TOP_CENTER, Anchor.CENTER, Anchor.BOTTOM_CENTER -> screenWidth / 2
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT -> screenWidth
        }
        val baseY = when (anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT -> 0
            Anchor.CENTER_LEFT, Anchor.CENTER, Anchor.CENTER_RIGHT -> screenHeight / 2
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT -> screenHeight
        }
        x = baseX + offsetX
        y = baseY + offsetY
    }

    companion object {
        fun renderElement(element: SkyHudElement, context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean) {
            val client = MinecraftClient.getInstance()
            element.width = client.textRenderer.getWidth(element.text)
            element.height = client.textRenderer.fontHeight

            element.calculatePosition(client.window.scaledWidth, client.window.scaledHeight)

            context.matrices.pushMatrix()
            context.matrices.scale(element.scale, element.scale)

            element.render(context, tickCounter, inEditor)

            if (inEditor) {
                context.drawBorder(element.x - 1, element.y - 1, element.width, element.height, Colors.GRAY)
            }

            context.matrices.popMatrix()
        }
    }

    abstract fun render(context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean)
}
