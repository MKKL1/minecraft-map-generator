package com.mkkl.minecraft.generation.surface;

import com.mkkl.minecraft.generation.GenerationSettings;
import com.mkkl.minecraft.generation.GeneratorData;
import com.mkkl.types.TerrainHeightMap;

public interface SurfaceGenerator {
    void generateColumn(GenerationSettings generationSettings, GeneratorData generatorData, TerrainHeightMap terrainHeightMap);
}
