package me.expandedcuber.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.expandedcuber.hud.HudManager;
import me.expandedcuber.hud.SkyHudElement;
import me.expandedcuber.item.SkyItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int heldItemTooltipFade;

    @Shadow private ItemStack currentStack;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private SpectatorHud spectatorHud;

    @Shadow protected abstract void renderHotbar(DrawContext context, RenderTickCounter tickCounter);

    @Shadow protected abstract void renderStatusBars(DrawContext context);

    @Shadow protected abstract void renderMountHealth(DrawContext context);

    @Shadow protected abstract InGameHud.BarType getCurrentBarType();

    @Shadow private Pair<InGameHud.BarType, Bar> currentBar;

    @Shadow @Final private Map<InGameHud.BarType, Supplier<Bar>> bars;

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        Profilers.get().push("selectedItemName");
        if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
            MutableText mutableText;

            if(this.currentStack.getItem() instanceof SkyItem skyItem) {
                mutableText = skyItem.getCustomName();
            } else {
                mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().getFormatting());
            }

            int i = this.getTextRenderer().getWidth(mutableText);
            int j = (context.getScaledWindowWidth() - i) / 2;
            int k = context.getScaledWindowHeight() - 59;
            if (!this.client.interactionManager.hasStatusBars()) {
                k += 14;
            }

            int l = (int)((float)this.heldItemTooltipFade * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                context.drawTextWithBackground(this.getTextRenderer(), mutableText, j, k, i, ColorHelper.withAlpha(l, -1));
            }
        }

        Profilers.get().pop();
    }

    @Inject(method = "renderMainHud", at = @At("HEAD"), cancellable = true)
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if ((this.client.interactionManager != null ? this.client.interactionManager.getCurrentGameMode() : null) == GameMode.SPECTATOR) {
            this.spectatorHud.renderSpectatorMenu(context);
        } else {
            this.renderHotbar(context, tickCounter);
        }

        if (this.client.interactionManager.hasStatusBars()) {
            this.renderStatusBars(context);
        }

        this.renderMountHealth(context);
        InGameHud.BarType barType = this.getCurrentBarType();
        if (barType != this.currentBar.getKey()) {
            this.currentBar = Pair.of(barType, (Bar)((Supplier<?>)this.bars.get(barType)).get());
        }

        ((Bar)this.currentBar.getValue()).renderBar(context, tickCounter);
        if (this.client.interactionManager.hasExperienceBar() && (this.client.player != null ? this.client.player.experienceLevel : 0) > 0) {
            Bar.drawExperienceLevel(context, this.client.textRenderer, this.client.player.experienceLevel);
        }

        ((Bar)this.currentBar.getValue()).renderAddons(context, tickCounter);
        if (this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
            this.renderHeldItemTooltip(context, ci);
        } else if (this.client.player != null && this.client.player.isSpectator()) {
            this.spectatorHud.render(context);
        }

        HudManager.INSTANCE.getEnabledElements().forEach(element -> {
            if(!element.getInitialized()) {
                element.setInitialized(true);
                element.setDefaultPos(context);
            }
            SkyHudElement.Companion.renderElement(element, context, tickCounter, false);
        });
    }
}
