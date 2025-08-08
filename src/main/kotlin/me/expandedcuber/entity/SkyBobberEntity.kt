package me.expandedcuber.entity

import me.expandedcuber.SkyBobberDuck
import me.expandedcuber.Skyterra
import me.expandedcuber.mixin.PlayerEntityMixin
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.FishingBobberEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class SkyBobberEntity(val thrower: PlayerEntity, world: World) : FishingBobberEntity(thrower, world, 0, 0) {
    init {
        this.setOwner(thrower)
        (thrower as SkyBobberDuck).`skyterra$setSkyBobber`(this)
    }

    override fun tick() {
        super.tick()
    }

    override fun use(usedItem: ItemStack?): Int {
        thrower.playSound(SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, 1f, 1f)

        this.discard()
        return 0
    }

    override fun remove(reason: RemovalReason?) {
        (thrower as SkyBobberDuck).`skyterra$setSkyBobber`(null)

        super.remove(reason)
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
        super.initDataTracker(builder)
    }
}