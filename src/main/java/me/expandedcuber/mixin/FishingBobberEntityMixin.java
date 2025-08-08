package me.expandedcuber.mixin;

import me.expandedcuber.item.ModItems;
import me.expandedcuber.item.SkyRod;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    @Redirect(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean customRemoveIfInvalid(ItemStack instance, Item item) {
        boolean vanilla = instance.isOf(item);
        boolean custom = instance.getItem() instanceof SkyRod;

        return vanilla || custom;
    }
}
