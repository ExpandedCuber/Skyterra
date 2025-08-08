package me.expandedcuber.util

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

object PlayerUtil {
    fun raycastTeleport(player: PlayerEntity, distance: Double): ActionResult {
        val eyePos = player.getCameraPosVec(1.0f)
        val lookDir = player.getRotationVec(1.0f)
        val targetPos = eyePos.add(lookDir.multiply(distance))

        val hit = player.world.raycast(
            RaycastContext(
                eyePos,
                targetPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
            )
        )

        when (hit.type) {
            HitResult.Type.BLOCK -> {
                val pos = hit.blockPos.up()

                player.requestTeleport(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)

                player.setVelocity(0.0, 0.0, 0.0)
                player.velocityModified = true

                return ActionResult.SUCCESS
            }
            HitResult.Type.MISS, HitResult.Type.ENTITY -> {
                val pos = hit.pos

                player.requestTeleport(pos.x, pos.y, pos.z)

                player.setVelocity(0.0, 0.0, 0.0)
                player.velocityModified = true

                player.fallDistance = 0.0

                return ActionResult.SUCCESS
            }
            else -> {
            }
        }

        return ActionResult.SUCCESS
    }

    fun raycastBlock(player: PlayerEntity, distance: Double): BlockPos? {
        val eyePos = player.getCameraPosVec(1.0f)
        val lookDir = player.getRotationVec(1.0f)
        val targetPos = eyePos.add(lookDir.multiply(distance))

        val hit = player.world.raycast(
            RaycastContext(
                eyePos,
                targetPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
            )
        )

        when (hit.type) {
            HitResult.Type.BLOCK -> {
                return hit.blockPos
            }
            else -> {
            }
        }

        return null
    }
}