package com.mkkl.minecraft.generation.features;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.TerrainManager;
import com.mkkl.minecraft.generation.ChunkGenerator;
import com.mkkl.types.Vector2;

import java.util.List;
import java.util.Random;

public class DecorationGenerator {

    private TreeGenerator treeGenerator;
    private TerrainManager terrainManager;
    public DecorationGenerator(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
    }

    public void setTreeGenerator(TreeGenerator treeGenerator) {
        this.treeGenerator = treeGenerator;
    }

    public void generateTrees(int x, int y) {
        Chunk chunk = terrainManager.getChunk(x, y);
        Random random = ChunkGenerator.createRandom(treeGenerator.seed, x, y);
        List<Vector2<Integer>> pointlist = treeGenerator.getPoints(chunk, random);
        int treecount = treeGenerator.trees.size();
        int id = chunk.getBlockPalette().addBlockToPalette("minecraft:oak_log");
        for(Vector2<Integer> point : pointlist) {
            int treeheight = random.nextInt(3)+4;
            int z = chunk.getHeight(point.x, point.y)+1;
            chunk.fillColumn(point.x, point.y, z, treeheight+z, id);
            chunk.setHeight(point.x, point.y, (short)(z+treeheight));
        }
        terrainManager.containsChunkLoaded(x-1, y+1);
    }
}
