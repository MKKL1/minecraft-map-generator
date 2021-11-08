package com.mkkl.minecraft.generation;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.generation.surface.SurfaceGenerator;
import com.mkkl.types.TerrainHeightMap;

public class ChunkGenerator {
    public Chunk generateChunk(SurfaceGenerator surfaceGenerator, GenerationSettings generationSettings, TerrainHeightMap terrainHeightMap, int xstart, int ystart) {
        GeneratorData generatorData = new GeneratorData();
        generatorData.chunk = new Chunk();
        generatorData.chunk.setXPos(xstart);
        generatorData.chunk.setYPos(ystart);
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                generatorData.xInWorld = (xstart*16)+i;
                generatorData.yInWorld = (ystart*16)+j;
                generatorData.xInChunk = i;
                generatorData.yInChunk = j;

                surfaceGenerator.generateColumn(generationSettings,generatorData, terrainHeightMap);
            }
        }

        return generatorData.chunk;
    }
}
