package com.mkkl.tasks;


import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.generation.ChunkGenerator;
import com.mkkl.minecraft.generation.GenerationSettings;
import com.mkkl.minecraft.generation.surface.SurfaceGenerator;
import com.mkkl.types.TerrainHeightMap;

import java.util.concurrent.Callable;

public class ChunkGenerationTask implements Callable<Chunk> {

    ChunkGenerator chunkGenerator;
    SurfaceGenerator surfaceGenerator;
    GenerationSettings generationSettings;
    TerrainHeightMap terrainHeightMap;
    int startx, starty;

    public ChunkGenerationTask(ChunkGenerator chunkGenerator, SurfaceGenerator sGenerator, GenerationSettings gSettings, TerrainHeightMap tHMap, int startx, int starty) {
        this.chunkGenerator = chunkGenerator;
        surfaceGenerator = sGenerator;
        generationSettings = gSettings;
        terrainHeightMap = tHMap;
        this.startx = startx;
        this.starty = starty;
    }

    @Override
    public Chunk call() throws Exception {
        return chunkGenerator.generateChunk(surfaceGenerator, generationSettings, terrainHeightMap, startx, starty);
    }
}
