package com.mkkl.minecraft;

import com.mkkl.constants;
import com.mkkl.types.Vector2;

public class Region {
    private Vector2<Integer> regioncoordinates;
    private Chunk[][] chunks;
    public Region() {
        chunks = new Chunk[constants.CHUNKS_PER_REGION][constants.CHUNKS_PER_REGION];
        regioncoordinates = new Vector2<>();
    }

    public Region(Vector2<Integer> regioncoordinates) {
        this();
        setRegionCoordinates(regioncoordinates);
    }

    public void setRegionCoordinates(int region_x, int region_y) {
        this.regioncoordinates.x = region_x;
        this.regioncoordinates.y = region_y;
    }

    public void setRegionCoordinates(Vector2<Integer> regioncoordinates) {
        this.regioncoordinates = regioncoordinates;
    }

    public Chunk getChunk(int region_x, int region_y) {
        return chunks[region_x][region_y];
    }

    public void setChunk(int region_x, int region_y, Chunk chunk) {
        chunks[region_x][region_y] = chunk;
    }
}
