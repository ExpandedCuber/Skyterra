package me.expandedcuber.hud

import me.expandedcuber.util.SkyRenderTickCounter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class GuiConfigScreen : Screen(Text.literal("Config")) {
    private var draggingElement: SkyHudElement? = null
    private var dragOffsetX = 0
    private var dragOffsetY = 0

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)

        HudManager.getEnabledElements().forEach { element ->
            SkyHudElement.renderElement(element, context, SkyRenderTickCounter(deltaTicks), true)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val client = MinecraftClient.getInstance()

        for (element in HudManager.getEnabledElements().reversed()) {
            val scaledX = element.x * element.scale
            val scaledY = element.y * element.scale
            val scaledWidth = element.width * element.scale
            val scaledHeight = element.height * element.scale

            if (mouseX >= scaledX && mouseX <= scaledX + scaledWidth &&
                mouseY >= scaledY && mouseY <= scaledY + scaledHeight) {

                draggingElement = element
                dragOffsetX = (mouseX - scaledX).toInt()
                dragOffsetY = (mouseY - scaledY).toInt()
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        draggingElement?.let { element ->
            val client = MinecraftClient.getInstance()
            val screenWidth = client.window.scaledWidth
            val screenHeight = client.window.scaledHeight

            val rawX = (mouseX - dragOffsetX) / element.scale
            val rawY = (mouseY - dragOffsetY) / element.scale

            val baseX = when (element.anchor) {
                Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT -> 0
                Anchor.TOP_CENTER, Anchor.CENTER, Anchor.BOTTOM_CENTER -> screenWidth / 2
                Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT -> screenWidth
            }
            val baseY = when (element.anchor) {
                Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT -> 0
                Anchor.CENTER_LEFT, Anchor.CENTER, Anchor.CENTER_RIGHT -> screenHeight / 2
                Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT -> screenHeight
            }

            element.offsetX = rawX.toInt() - baseX
            element.offsetY = rawY.toInt() - baseY

            return true
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        draggingElement = null
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun close() {
        super.close()
        HudManager.saveToConfig()
    }
}