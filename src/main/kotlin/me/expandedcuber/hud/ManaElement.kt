package me.expandedcuber.hud

import me.expandedcuber.stats.Stat
import me.expandedcuber.stats.PlayerStats
import me.expandedcuber.util.TextUtil
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Colors

class ManaElement : SkyHudElement() {
    override var id: String = "mana_display"
    override var x: Int = 0
    override var y: Int = 0
    override var enabled: Boolean = true
    override var width: Int = 0
    override var height: Int = 0
    override var text: MutableText = Text.empty()

    override fun setDefaultPos(context: DrawContext) {
        val client = MinecraftClient.getInstance()
        this.x = ((client.window.scaledWidth - width) / 2) + 60
        this.y = ((client.window.scaledHeight * 3) / 4) + 10
        this.scale = 0.5f
    }

    override fun render(context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean) {
        if(inEditor) {
            this.text = TextUtil.literal("%aqua%123/123✎ Mana")
        } else {
            this.text = TextUtil.literal("%aqua%${PlayerStats.get(Stat.MANA).toInt()}/${Stat.MANA.default.toInt()}✎ Mana")
        }

        val client = MinecraftClient.getInstance()

        context.drawText(
            client.textRenderer,
            text,
            this.x, this.y,
            Colors.BLUE,
            true
        )
    }

}
