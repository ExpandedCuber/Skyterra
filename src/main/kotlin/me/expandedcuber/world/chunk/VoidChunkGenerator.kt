package me.expandedcuber.world.chunk

import com.google.common.collect.Sets
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.SharedConstants
import net.minecraft.block.Blocks
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.ChunkRegion
import net.minecraft.world.HeightLimitView
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.StructureWeightSampler
import net.minecraft.world.gen.chunk.*
import net.minecraft.world.gen.noise.NoiseConfig
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import java.util.function.Supplier


class VoidChunkGenerator(
    biomeSource: BiomeSource
) : ChunkGenerator(biomeSource) {
    private lateinit var settings: RegistryEntry<ChunkGeneratorSettings>

    companion object {
        val CODEC: MapCodec<VoidChunkGenerator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                BiomeSource.CODEC.fieldOf("biome_source")
                    .forGetter { it.biomeSource },
                ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter { it.settings }
            ).apply(instance, ::VoidChunkGenerator)
        }
    }

    constructor(biomeSource: BiomeSource, settings: RegistryEntry<ChunkGeneratorSettings>): this(biomeSource) {
        this.settings = settings
    }

    override fun getCodec(): MapCodec<out ChunkGenerator?>? {
        return CODEC
    }

    override fun carve(
        chunkRegion: ChunkRegion?,
        seed: Long,
        noiseConfig: NoiseConfig?,
        biomeAccess: BiomeAccess?,
        structureAccessor: StructureAccessor?,
        chunk: Chunk?
    ) {
    }

    override fun buildSurface(
        region: ChunkRegion,
        structures: StructureAccessor,
        noiseConfig: NoiseConfig,
        chunk: Chunk
    ) {
        if(chunk.pos.x == 0 && chunk.pos.z == 0) {
            chunk.setBlockState(BlockPos(0, 64, 0), Blocks.BEDROCK.defaultState)
        }
    }

    override fun populateEntities(region: ChunkRegion?) {
    }

    override fun getWorldHeight(): Int {
        return 384
    }

    override fun populateNoise(
        blender: Blender?,
        noiseConfig: NoiseConfig?,
        structureAccessor: StructureAccessor,
        chunk: Chunk
    ): CompletableFuture<Chunk?> {
        return CompletableFuture.completedFuture(chunk)
    }

    override fun getSeaLevel(): Int {
        return 0
    }

    override fun getMinimumY(): Int {
        return -64
    }

    override fun getHeight(
        x: Int,
        z: Int,
        heightmap: Heightmap.Type?,
        world: HeightLimitView?,
        noiseConfig: NoiseConfig?
    ): Int {
        return 320
    }

    override fun getColumnSample(
        x: Int,
        z: Int,
        world: HeightLimitView,
        noiseConfig: NoiseConfig
    ): VerticalBlockSample {
        val height = worldHeight - minimumY

        val blockStates = Array(height) { Blocks.AIR.defaultState }

        return VerticalBlockSample(world.bottomY, blockStates)
    }

    override fun appendDebugHudText(
        text: List<String?>?,
        noiseConfig: NoiseConfig?,
        pos: BlockPos?
    ) {
    }
}