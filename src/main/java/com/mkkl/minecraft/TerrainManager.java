package com.mkkl.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mkkl.types.Vector2;
import com.mkkl.types.Vector3;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TerrainManager {
    private final ConcurrentHashMap<Point, Region> regionList;
    private final ConcurrentHashMap<Point, Region> unloadedRegionList;
    public Vector2<Integer> chunkSize;
    public Vector2<Integer> regionSize;
    private boolean unload = true;

    public TerrainManager() {
        regionList = new ConcurrentHashMap<>();
        unloadedRegionList = new ConcurrentHashMap<>();
    }

    public synchronized void unloadRegion(int xPos, int yPos) {
        Region region = getRegion(xPos, yPos);

        Point point = new Point(region.xPos, region.yPos);
        region.clearChunksData();
        unloadedRegionList.put(point,region);
        regionList.remove(point);
        //TODO: Save to mca file
    }

    public ConcurrentHashMap<Point, Region> getRegionList() {
        return regionList;
    }

    public Region getRegion(int xPos, int yPos, boolean load) {
        if (!load)
            return getRegion(xPos, yPos);
        else if (containsRegionLoaded(xPos, yPos)) {
            return getRegion(xPos, yPos);
        } else {
            return null;
            //TODO: Load region from file
        }
    }

    public Region getRegion(int xPos, int yPos) {
        Region region;
        Point point = new Point(xPos, yPos);
        region = regionList.get(point);
        if (region != null) return region;
        return unloadedRegionList.get(point);
    }

    public Region getRegionLoaded(int xPos, int yPos) {
        return regionList.get(new Point(xPos, yPos));
    }

    public boolean containsRegionLoaded(int xPos, int yPos) {
        Point point = new Point(xPos, yPos);
        return regionList.containsKey(point);
    }

    public boolean containsRegion(int xPos, int yPos) {
        Point point = new Point(xPos, yPos);
        return regionList.containsKey(point) || unloadedRegionList.containsKey(point);
    }

    public Region getRegionOrCreate(int xPos, int yPos) {
        if (containsRegion(xPos, yPos)) return getRegion(xPos, yPos);
        else {
            Region region = new Region();
            region.setRegionCoordinates(xPos, yPos);
            addRegion(region);
            return region;
        }
    }

    public Region getRegionByCords(int xInWorld, int yInWorld) {
        return getRegion(xInWorld/512, yInWorld/512);
    }

    public void addRegion(Region region) {
        regionList.put(new Point(region.xPos, region.yPos), region);
    }

    public void setBlock(int x, int y, int z, String name) {
        getRegion(x/512, y/512).getChunk(x%512, y%512).setBlock(x%16, y%16, z, name);
    }

    public void setChunk(int xPos, int yPos, Chunk chunk) {
        getRegionOrCreate(xPos/32, yPos/32).setChunk(xPos%32, yPos%32, chunk);
    }
    public Chunk getChunk(int xPos, int yPos) {
        return getRegion(xPos/32, yPos/32).getChunk(xPos%32, yPos%32);
    }

    public boolean containsChunk(int xPos, int yPos) {
        if (containsRegion(xPos/32, yPos/32))
            return getRegion(xPos/32, yPos/32).containsChunk(xPos%32, yPos%32);
        else return false;
    }

    public boolean containsChunkLoaded(int xPos, int yPos) {
        if (containsRegionLoaded(xPos/32, yPos/32))
            return getRegionLoaded(xPos/32, yPos/32).containsChunk(xPos%32, yPos%32);
        else return false;
    }


    public int getHightestPoint() {
        int maxheight = 0;
        for(Region region: regionList.values()) {
            for(Chunk[] chunks: region.getChunkArray()) {
                    for(Chunk chunk: chunks)
                    if(chunk != null && chunk.getMaxHeight() > maxheight) maxheight = chunk.getMaxHeight();
            }
        }
        return maxheight;
    }

    private final int[][] nearRegions = new int[][] {{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1},{0,1},{1,1}};

    private int countFinishedNear(int xRegion, int yRegion) {
        int c = 0;
        Region tmp;
        for (int[] point : nearRegions) {
            if(unloadedRegionList.containsKey(new Point(xRegion+point[0], yRegion+point[1]))) {
                c++;
            } else {
                if ((tmp = getRegionLoaded(xRegion+point[0], yRegion+point[1])) != null && tmp.isFinished()) {
                    c++;
                }
            }
            if (xRegion+point[0] < 0 || xRegion+point[0] > regionSize.x-1
                    || yRegion+point[1] < 0 || yRegion+point[1] > regionSize.y-1) {
                c++;
            }
        }
        return c;
    }

    public void shouldUnloadUnusedRegions(boolean b) {
        unload = b;
    }


    public void updateFinishedRegions(int xRegion, int yRegion) {
        Region region = getRegion(xRegion, yRegion);
        region.setFinished();

        if (!unload) return;

        List<Region> finishedRegionNear = new ArrayList<>();
        Region i;
        for (int[] point : nearRegions) {
            if ((i = getRegionLoaded(xRegion+point[0], yRegion+ point[1])) != null && i.isFinished()) {
                finishedRegionNear.add(i);
            }
        }

        for(Region e: finishedRegionNear) {
            int c = countFinishedNear(e.xPos, e.yPos);
            if (c>=8) {
                unloadRegion(e.xPos, e.yPos);
            }
        }
    }
}
