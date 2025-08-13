package me.expandedcuber.mixin;

import com.google.common.collect.Lists;
import me.expandedcuber.Skyterra;
import me.expandedcuber.components.ItemStatComponent;
import me.expandedcuber.components.ModDataComponents;
import me.expandedcuber.item.SkyItem;
import me.expandedcuber.stats.Stat;
import me.expandedcuber.util.Rarity;
import net.fabricmc.fabric.mixin.transfer.ItemMixin;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Shadow public abstract Item getItem();

    @Shadow public abstract <T extends TooltipAppender> void appendComponentTooltip(ComponentType<T> componentType, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type);

    @Shadow public abstract Text getItemName();

    @Inject(method = "getFormattedName", at = @At("HEAD"), cancellable = true)
    public void getFormattedName(CallbackInfoReturnable<Text> cir) {
        if (this.getItem() instanceof SkyItem skyItem) {
            cir.setReturnValue(skyItem.getCustomName());
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    public void getName(CallbackInfoReturnable<Text> cir) {
        if (this.getItem() instanceof SkyItem skyItem) {
            cir.setReturnValue(skyItem.getCustomName());
        }
    }

    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    public void getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> list = Lists.newArrayList();
        TooltipDisplayComponent tooltipDisplayComponent = (TooltipDisplayComponent)this.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);

        if(tooltipDisplayComponent == null) tooltipDisplayComponent = TooltipDisplayComponent.DEFAULT;
        MutableText name = Text.empty();

        if(this.getItem() instanceof SkyItem skyItem) {
            name.append(skyItem.getCustomName()).withColor(skyItem.getRarity().getColor().getRgb());
        } else {
            name.append(this.getItem().getName()).withColor(Rarity.Common.getColor().getRgb());
        }

        list.add(name);
        Objects.requireNonNull(list);
        this.appendTooltip(context, tooltipDisplayComponent, player, type, list::add, cir);
        cir.setReturnValue(list);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    public void appendTooltip(Item.TooltipContext context, TooltipDisplayComponent displayComponent, PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
        ItemStatComponent stats = this.get(ModDataComponents.INSTANCE.getITEM_STAT());
        double breakingPower;

        if(stats != null) {
            breakingPower = stats.getStats().getOrDefault(Stat.BREAKING_POWER, 0.0);
        } else {
            breakingPower = 0.0;
        }

        if(this.getItem() instanceof SkyItem skyItem) {
            List<Text> lore = skyItem.getLore();
            if(breakingPower > 0.0) textConsumer.accept(Stat.BREAKING_POWER.getLore(breakingPower));

            if(stats != null) {
                stats.getStats().forEach((stat, value) -> {
                    if(stat != Stat.BREAKING_POWER) textConsumer.accept(stat.getLore(value));
                });
            }

            for(Text line : lore) {
                textConsumer.accept(line);
            }

            ci.cancel();
        } else {
            this.appendComponentTooltip(ModDataComponents.INSTANCE.getITEM_STAT(), context, displayComponent, textConsumer, type);
            textConsumer.accept(Text.empty());
            textConsumer.accept(Rarity.Common.getText(false).formatted(Formatting.BOLD));
        }
    }
}
