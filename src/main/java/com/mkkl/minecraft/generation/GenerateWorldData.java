package com.mkkl.minecraft.generation;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.TerrainModel;
import com.mkkl.minecraft.generation.surface.CoveredSurface;
import com.mkkl.minecraft.generation.surface.FullSurface;
import com.mkkl.minecraft.generation.surface.HollowSurface;
import com.mkkl.minecraft.generation.surface.SurfaceGenerator;
import com.mkkl.tasks.ChunkGenerationTask;
import com.mkkl.tasks.TaskManager;
import com.mkkl.types.TerrainHeightMap;
import com.mkkl.types.Vector3;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GenerateWorldData {

    TerrainHeightMap terrainHeightMap;
    GenerationSettings _generationSettings = new GenerationSettings();
    SurfaceGenerator _surfaceGenerator;
    List<Chunk> chunks;
    int chunkxsize;
    int chunkysize;
    int threadcount = 0;

    //Have to find some better way to handle storing blocks id's

    public GenerateWorldData(TerrainHeightMap terrainHeightMap) {
        setTerrainHeightMap(terrainHeightMap);
    }

    public void setTerrainHeightMap(TerrainHeightMap terrainHeightMap) {
        this.terrainHeightMap = terrainHeightMap;
    }

    public GenerateWorldData setInfillType(TerrainInfill terrainInfill) {
        _generationSettings.infill = terrainInfill;
        switch (_generationSettings.infill){
            case HOLLOW -> _surfaceGenerator = new HollowSurface();
            case COVERED -> _surfaceGenerator = new CoveredSurface();
            case FULL -> _surfaceGenerator = new FullSurface();
            default -> throw new IllegalStateException("Unexpected value: " + _generationSettings.infill);
        }
        return this;
    }

    public GenerateWorldData setSurfaceGenerator(SurfaceGenerator surfaceGenerator) {
        _surfaceGenerator = surfaceGenerator;
        return this;
    }

    public GenerateWorldData setGenerationSettings(GenerationSettings generationSettings) {
        _generationSettings = generationSettings;
        return this.setInfillType(generationSettings.infill);
    }

    public GenerateWorldData setThreadCount(int threadcount) {
        this.threadcount = threadcount;
        return this;
    }


    public GenerateWorldData generate(GenerationSettings generationSettings) throws ExecutionException, InterruptedException {
        return this.setGenerationSettings(generationSettings).generate();
    }

    public GenerateWorldData generate() throws ExecutionException, InterruptedException {
        if (_generationSettings == null) _generationSettings = new GenerationSettings();

        chunkxsize = terrainHeightMap.size.x/16;
        chunkysize = terrainHeightMap.size.y/16;

        TaskManager taskManager;
        if (threadcount < 1)
            taskManager = new TaskManager();
        else
            taskManager = new TaskManager(threadcount);

        ChunkGenerator chunkGenerator = new ChunkGenerator();
        List<Future<Chunk>> finishedchunks = new ArrayList<>();

        for(int i = 0; i < chunkxsize; i ++)
            for(int j = 0; j < chunkysize; j++) {
                finishedchunks.add(
                        taskManager.add(
                                new ChunkGenerationTask(chunkGenerator, _surfaceGenerator, _generationSettings, terrainHeightMap, i, j)));
        }

        chunks = new ArrayList<>();
        for(Future<Chunk> finishedtask: finishedchunks)
            chunks.add(finishedtask.get());

        taskManager.close();
        return this;
    }

    public TerrainModel getTerrainModel() {
        int maxheight = 0;
        Chunk[][] chunkarray = new Chunk[chunkxsize][chunkysize];
        for(Chunk chunk: chunks) {
            if(chunk.getMaxHeight() > maxheight) maxheight = chunk.getMaxHeight();
            chunkarray[chunk.getXPos()][chunk.getYPos()] = chunk;
        }

        maxheight = Math.round(terrainHeightMap.maxheight-terrainHeightMap.minheight)+1;
        TerrainModel terrainModel = new TerrainModel(chunkarray, new Vector3<Integer>(chunkxsize*16, chunkysize*16, maxheight+1));
        return terrainModel;
    }

}
