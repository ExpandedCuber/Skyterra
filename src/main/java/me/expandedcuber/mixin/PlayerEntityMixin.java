package me.expandedcuber.mixin;

import me.expandedcuber.SkyBobberDuck;
import me.expandedcuber.entity.SkyBobberEntity;
import me.expandedcuber.stats.Stat;
import me.expandedcuber.stats.PlayerStats;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements SkyBobberDuck {
    @Unique private SkyBobberEntity skyBobber = null;

    @Override
    public SkyBobberEntity skyterra$getSkyBobber() {
        return this.skyBobber;
    }

    @Unique
    public void skyterra$setSkyBobber(SkyBobberEntity skyBobber) {
        this.skyBobber = skyBobber;
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean modifyCrit(boolean value) {
        return false;
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        float speedStat = (float)PlayerStats.INSTANCE.get(Stat.MINING_SPEED);

        float baseSpeed = cir.getReturnValueF();
        float speed = speedStat / 100.0f;

        cir.setReturnValue(Math.max(speed, 1f) * baseSpeed);
    }
}
