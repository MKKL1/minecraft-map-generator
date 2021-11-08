package com.mkkl.minecraft.generation.surface;

import com.mkkl.minecraft.generation.GenerationSettings;
import com.mkkl.minecraft.generation.GeneratorData;
import com.mkkl.types.TerrainHeightMap;

public class HollowSurface implements SurfaceGenerator{
    @Override
    public void generateColumn(GenerationSettings generationSettings, GeneratorData generatorData, TerrainHeightMap terrainHeightMap) {
        int z = Math.round(terrainHeightMap.getPoint(generatorData.xInWorld,generatorData.yInWorld) - terrainHeightMap.minheight);
        generatorData.chunk.setMaxHeight(z);
        if (z < 0) generatorData.chunk.setBlock(generatorData.xInChunk,generatorData.yInChunk,0, generationSettings.errorid);
        else {
            generatorData.chunk.setBlock(generatorData.xInChunk,generatorData.yInChunk,z, generationSettings.baseid);
        }
    }
}
