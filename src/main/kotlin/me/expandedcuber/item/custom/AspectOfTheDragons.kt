package me.expandedcuber.item.custom

import me.expandedcuber.util.Rarity
import me.expandedcuber.Skyterra
import me.expandedcuber.util.TextUtil
import me.expandedcuber.item.ItemAbility
import me.expandedcuber.item.ItemAbilityType
import me.expandedcuber.item.SkySword
import net.minecraft.client.sound.Sound
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageSources
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class AspectOfTheDragons(settings: Settings) : SkySword(settings) {
    class DragonRageAbility(item: Item)
        : ItemAbility(item, "Dragon Rage",
            listOf(
                TextUtil.literal("%no_italic%%gray%All Monsters in front of you take %green%12,000"),
                TextUtil.literal("%no_italic%%gray%damage. Hit monsters take large knockback.")
            ), 100, 0, ItemAbilityType.RightClick
    ) {
        override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
            world.playSound(player, player.blockPos.add(0, 1, 0), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS)
            val looking = player.getRotationVec(1.0f)
            val radius = 10.0
            val box = Box(
                player.x - radius, player.x - 3.5, player.z - radius,
                player.x + radius, player.y + 3.5, player.z + radius
            )
            val entities = world.getOtherEntities(null, box)
            val threshold = 0.9

            val entitiesInFront = entities.stream()
                .filter { entity ->
                    val toEntity = entity.pos.subtract(player.pos).normalize()
                    toEntity.dotProduct(looking) > threshold
                }
                .toList()

            val particleRadius = 1.2
            val heightStep = 0.05
            val angleStep = Math.PI / 8
            val base = player.pos

            for(i in 1..30) {
                val theta = i * angleStep
                val xOffset = cos(theta) * particleRadius
                val yOffset = i * heightStep
                val zOffset = sin(theta) * particleRadius
                val velo = Vec3d(looking.x * i * heightStep, looking.y * i * heightStep, looking.z * i * heightStep)

                world.addParticleClient(
                    ParticleTypes.FLAME,
                    base.x + xOffset,
                    base.y + yOffset,
                    base.z + zOffset,
                    velo.x,
                    velo.y,
                    velo.z
                )
            }

            entitiesInFront.forEach { entity ->
                if(entity is LivingEntity && entity != player && !world.isClient) {
                    val world = world as ServerWorld
                    val source = DamageSource(
                        world.registryManager
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(Skyterra.DRAGON_RAGE_DAMAGE.value).get()
                    )

                    entity.damage(world, source, 12000f)
                }
            }

            return ActionResult.SUCCESS
        }
    }

    init {
        this.rarity = Rarity.Legendary
        this.abilities = listOf(DragonRageAbility(this)).toMutableList()
    }
}