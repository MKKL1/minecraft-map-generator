package com.mkkl.minecraft;

import com.mkkl.constants;
import com.mkkl.types.Vector2;

public class Region {
    public int xPos;
    public int yPos;
    private boolean finished;

    private final Chunk[][] chunks;
    public Region() {
        chunks = new Chunk[constants.CHUNKS_PER_REGION][constants.CHUNKS_PER_REGION];
    }

    public Region(int region_x, int region_y) {
        this();
        setRegionCoordinates(region_x, region_y);
    }
    public void setRegionCoordinates(int region_x, int region_y) {
        this.xPos = region_x;
        this.yPos = region_y;
    }

    public Chunk getChunk(int region_x, int region_y) {
        return chunks[region_x][region_y];
    }

    public Chunk getChunkByCords(int x, int y) {
        return getChunk(x/32, y/32);
    }

    public Chunk[][] getChunkArray() {
        return chunks;
    }

    public void setChunk(int region_x, int region_y, Chunk chunk) {
        chunks[region_x][region_y] = chunk;
    }

    public boolean containsChunk(int x, int y) {
        if (x < 0 || x > 32 || y < 0 || y > 32) return false;
        return chunks[x][y] != null;
    }

    public void setFinished() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void clearChunksData() {
        for (Chunk[] a: chunks) {
            for(Chunk b: a) {
                b.clear();
            }
        }
    }
}
