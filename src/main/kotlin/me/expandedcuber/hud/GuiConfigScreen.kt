package me.expandedcuber.hud

import me.expandedcuber.util.SkyRenderTickCounter
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderTickCounter
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
        for (element in HudManager.getEnabledElements().reversed()) {
            val scaledX = element.x * element.scale
            val scaledY = element.y * element.scale
            val scaledWidth = element.width * element.scale
            val scaledHeight = element.height * element.scale

            if (mouseX >= scaledX && mouseX <= scaledX + scaledWidth &&
                mouseY >= scaledY && mouseY <= scaledY + scaledHeight) {
                draggingElement = element as SkyHudElement?
                dragOffsetX = (mouseX - element.x * element.scale).toInt()
                dragOffsetY = (mouseY - element.y * element.scale).toInt()
                println("Dragging")
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        draggingElement?.let {
            it.x = ((mouseX - dragOffsetX) / it.scale).toInt()
            it.y = ((mouseY - dragOffsetY) / it.scale).toInt()

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