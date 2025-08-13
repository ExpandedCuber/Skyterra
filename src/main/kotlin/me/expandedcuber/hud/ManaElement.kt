package me.expandedcuber.hud

import me.expandedcuber.config.Config
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
    override var scaledWidth: Int = 0
    override var scaledHeight: Int = 0
    override var scaledX: Int = 0
    override var scaledY: Int = 0
    override var text: MutableText = Text.empty()

    override fun setDefaultPos() {
        val configPos = Config.getHudElementPosition(this)

        if (configPos != null) {
            this.x = configPos.x
            this.y = configPos.y
            this.scale = 0.5f
        } else {
            anchor = Anchor.BOTTOM_CENTER
            offsetX = 60
            offsetY = -30
            scale = 0.5f
        }
    }

    override fun render(context: DrawContext, tickCounter: RenderTickCounter, inEditor: Boolean) {
        this.scaledWidth = (this.width * this.scale).toInt()
        this.scaledHeight = (this.height * this.scale).toInt()
        this.scaledX = (this.x * this.scale).toInt()
        this.scaledY = (this.y * this.scale).toInt()

        this.text = TextUtil.literal("%aqua%${PlayerStats.get(Stat.MANA).toInt()}/${Stat.MANA.default.toInt()}✎ Mana")

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
