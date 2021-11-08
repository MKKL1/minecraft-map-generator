package com.mkkl.minecraft.generation.surface;

import com.mkkl.minecraft.generation.GenerationSettings;
import com.mkkl.minecraft.generation.GeneratorData;
import com.mkkl.types.TerrainHeightMap;

public class CoveredSurface implements SurfaceGenerator{
    @Override
    public void generateColumn(GenerationSettings generationSettings, GeneratorData generatorData, TerrainHeightMap terrainHeightMap) {
        int z = Math.round(terrainHeightMap.getPoint(generatorData.xInWorld,generatorData.yInWorld) - terrainHeightMap.minheight);
        generatorData.chunk.setMaxHeight(z);
        if (z < 0) generatorData.chunk.setBlock(generatorData.xInChunk,generatorData.yInChunk,0, generationSettings.errorid);
        else {
            float lowestpoint = getLowestPointAround(terrainHeightMap, generatorData.xInWorld,generatorData.yInWorld);
            int zdiff = Math.round(z - lowestpoint);

            if (zdiff > 1) {
                generatorData.chunk.fillBlocks(
                        generatorData.xInChunk, generatorData.yInChunk, z-1,

                        generatorData.xInChunk, generatorData.yInChunk, Math.round(lowestpoint),
                        generationSettings.baseid);

                generatorData.chunk.setBlock(generatorData.xInChunk,generatorData.yInChunk,z, generationSettings.topid);
            }
            else generatorData.chunk.setBlock(generatorData.xInChunk,generatorData.yInChunk,z, generationSettings.topid);
        }
    }

    private float getLowestPointAround(TerrainHeightMap terrainHeightMap, int x, int y) {
        float[] points = {
                terrainHeightMap.getPoint(x-1,y),
                terrainHeightMap.getPoint(x,y-1),
                terrainHeightMap.getPoint(x+1,y),
                terrainHeightMap.getPoint(x,y+1)
        };
        float min = Float.MAX_VALUE;
        for(float v : points) {
            if (v<min) min = v;
        }
        return min;

    }
}
