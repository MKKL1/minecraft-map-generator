package com.mkkl.minecraft.generation;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.Region;
import com.mkkl.minecraft.TerrainManager;
import com.mkkl.minecraft.TerrainModel;
import com.mkkl.minecraft.generation.features.DecorationGenerator;
import com.mkkl.minecraft.generation.features.TreeGenerator;
import com.mkkl.minecraft.generation.surface.CoveredSurface;
import com.mkkl.minecraft.generation.surface.FullSurface;
import com.mkkl.minecraft.generation.surface.HollowSurface;
import com.mkkl.minecraft.generation.surface.SurfaceGenerator;
import com.mkkl.tasks.TaskManager;
import com.mkkl.types.TerrainHeightMap;
import com.mkkl.types.Vector2;
import com.mkkl.types.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateWorldData {

    Logger logger;
    TerrainHeightMap terrainHeightMap;
    GenerationSettings _generationSettings = new GenerationSettings();
    SurfaceGenerator _surfaceGenerator;
    DecorationGenerator _decorationGenerator;
    TerrainManager terrainManager = new TerrainManager();
    int chunkxsize;
    int chunkysize;
    int threadcount = 0;
    boolean decorate;

    public GenerateWorldData(TerrainHeightMap terrainHeightMap) {
        setTerrainHeightMap(terrainHeightMap);
        _decorationGenerator = new DecorationGenerator(terrainManager);
    }

    public GenerateWorldData setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public GenerateWorldData setTerrainHeightMap(TerrainHeightMap terrainHeightMap) {
        this.terrainHeightMap = terrainHeightMap;
        return this;
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

    public GenerateWorldData setTreeGenerator(TreeGenerator treeGenerator) {
        _decorationGenerator.setTreeGenerator(treeGenerator);
        decorate = true;
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
        terrainManager.chunkSize = new Vector2<>(chunkxsize, chunkysize);
        int chunkcount = chunkxsize*chunkysize;

        long startTime = System.nanoTime();
        if (logger != null) {
            logger.log(Level.INFO, "Starting generation of {0} chunks", chunkcount);
            logger.log(Level.INFO, "Around {0}mb is needed to run generation smoothly",
                    Math.round(((long)chunkcount*16*16*terrainHeightMap.zSize())/1000000));
        }


        TaskManager taskManager;
        if (threadcount < 1)
            taskManager = new TaskManager();
        else
            taskManager = new TaskManager(threadcount);


        ChunkGenerator chunkGenerator = new ChunkGenerator(terrainManager);

        int regionsizex = Math.round(((float)chunkxsize/32) + 0.5f);
        int regionsizey = Math.round(((float)chunkysize/32) + 0.5f);
        terrainManager.regionSize = new Vector2<>(regionsizex, regionsizey);
        int regioncount = regionsizex*regionsizey;

        final int[] finishedregions = {0};
        final long[] nextUpdate = {System.nanoTime() + 5000000000L};
        final float[] exectime = {0};

        for(int i = 0; i < regionsizex; i ++)
            for(int j = 0; j < regionsizey; j++) {

                int finalI = i;
                int finalJ = j;
                taskManager.add(new Runnable() {
                    @Override
                    public void run() {
                        int chunksx = 32;
                        int chunksy = 32;

                        //Check if region is last in row or column
                        if (regionsizex-1== finalI) {
                            if (chunkxsize%32 != 0) chunksx = chunkxsize%32;
                            else chunksx = 0;
                        }
                        if (regionsizey-1== finalJ) {
                            if (chunkysize%32 != 0) chunksy = chunkysize%32;
                            else chunksy = 0;
                        }

                        int startchunkinregionX = finalI*32;
                        int startchunkinregionY = finalJ*32;

                        Region region = terrainManager.getRegionOrCreate(finalI, finalJ);

                        for(int k = 0; k < chunksx; k ++) {
                            for (int l = 0; l < chunksy; l++) {
                                Chunk chunk = chunkGenerator.generateChunk(
                                        region,
                                        _surfaceGenerator,
                                        _generationSettings,
                                        terrainHeightMap,
                                        startchunkinregionX + k, startchunkinregionY + l);
                                terrainManager.setChunk(startchunkinregionX + k, startchunkinregionY + l, chunk);

                                if(decorate)
                                    chunkGenerator.decorateChunk(_decorationGenerator, chunk);
                            }
                        }
                        terrainManager.updateFinishedRegions(finalI, finalJ);

                        finishedregions[0]++;
                        if (logger != null) {
                            if (System.nanoTime() > nextUpdate[0]) {

                                exectime[0] += (float)(5000000000L+System.nanoTime()-nextUpdate[0])/1000000000L;
                                float remainingTime = (exectime[0]/finishedregions[0])*(regioncount-finishedregions[0]);

                                logger.log(Level.INFO, "Finished region {0}/{1}, remaining time {2}s", new Object[]{
                                        finishedregions[0], regioncount, remainingTime

                                });

                                nextUpdate[0] = System.nanoTime() + 5000000000L;
                            }
                        }
                    }
                });
            }

        taskManager.awaitTerminationAfterShutdown();
        if (logger != null) {
            int nullchunks = 0;
            for(int i = 0; i < chunkxsize; i ++)
                for(int j = 0; j < chunkysize; j++) {
                    if (!terrainManager.containsChunk(i, j)) nullchunks++;
                }
            logger.log(Level.INFO,
                    "Generation finished: generated chunks {0}({2} regions) and {3} empty chunks, time elapsed {1}s",
                    new Object[]{chunkcount, (float)(System.nanoTime() - startTime)/1000000000, regioncount, nullchunks});
        }
        return this;
    }

    /**
     * Doesn't support multi-threading for saving to terrain model
     */
    public TerrainModel generateTerrainModel() throws ExecutionException, InterruptedException {
        terrainManager.shouldUnloadUnusedRegions(false);
        generate();
        return new TerrainModel(terrainManager, new Vector3<Integer>(chunkxsize*16, chunkysize*16, terrainManager.getHightestPoint()+1));
    }

    /**
     * {@inheritDoc}
     */
    public TerrainModel generateTerrainModel(GenerationSettings generationSettings) throws ExecutionException, InterruptedException {
        return this.setGenerationSettings(generationSettings).generateTerrainModel();
    }

    public TerrainManager generateTerrainManager() throws ExecutionException, InterruptedException {
        terrainManager.shouldUnloadUnusedRegions(false);
        generate();
        return terrainManager;
    }

    public GenerateWorldData save() {
        return this;
    }


}
