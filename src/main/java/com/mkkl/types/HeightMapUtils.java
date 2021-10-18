package com.mkkl.types;

import com.mkkl.ScalingAlgorithms;
import com.mkkl.minecraft.TerrainModel;

public class HeightMapUtils {

    /**
     * Changes real world distance between each data point (meters)
     * */
    public static TerrainHeightMap scaleToResolution(TerrainHeightMap terrainHeightMap, Vector2<Float> newresolution) {
        float scalefactorX = (terrainHeightMap.getResolution().x/newresolution.x);
        float scalefactorY = (terrainHeightMap.getResolution().y/newresolution.y);
        TerrainHeightMap scaledmap;
        if(scalefactorX > 1) {
            scaledmap = new TerrainHeightMap(ScalingAlgorithms.bilinearInterpolation(terrainHeightMap.getHeightmap(), scalefactorX, scalefactorY));
        } else {
            scaledmap = new TerrainHeightMap(ScalingAlgorithms.nearestNeighbor(terrainHeightMap.getHeightmap(), scalefactorX, scalefactorY));
        }
        scaledmap.setResolution(newresolution);
        return scaledmap;
    }
    /**
     * Scales real world distance between each data point
     * @param scalefactor e.g. {@code scalefactor = new Vector2<Float>(2f, 2f);} would change {@link TerrainHeightMap#resolution} of each x and y from 1 to 0.5
     */
    public static TerrainHeightMap scaleMap(TerrainHeightMap terrainHeightMap, Vector2<Float> scalefactor) {
        return scaleToResolution(terrainHeightMap, new Vector2<>(terrainHeightMap.getResolution().x/scalefactor.x, terrainHeightMap.getResolution().y/scalefactor.y));
    }

    /**
     * Scales height map.
     * That includes height as well.
     * @param scalefactor similar to {@link #scaleMap(TerrainHeightMap, Vector2)} but it with z axis included
     * */
    public static TerrainHeightMap scaleMap(TerrainHeightMap terrainHeightMap, Vector3<Float> scalefactor) {
        Vector2<Float> newresolution = new Vector2<>(terrainHeightMap.getResolution().x/scalefactor.x, terrainHeightMap.getResolution().y/scalefactor.y);
        TerrainHeightMap newmap = scaleToResolution(terrainHeightMap, newresolution);

        for(int x = 0; x < newmap.size.x; x++) {
            for(int y = 0; y < newmap.size.y; y++) {
                newmap.setPoint(x,y,newmap.getPoint(x,y)*scalefactor.z);
            }
        }
        newmap.recalculateExtremes();
        return newmap;
    }
    /**
     * Takes each data point from {@link TerrainHeightMap}
     * and converts it to schematic ready minecraft {@link TerrainModel}
     * <p>
     * Use {@link TerrainHeightMap#setResolution(float, float)} to set resolution to 1 for 1:1 conversion (1 block = 1 meter).
     * That can be done with {@link HeightMapUtils#scaleToResolution(TerrainHeightMap, Vector2)}
     * */
    public static TerrainModel heightMapToMcModel(TerrainHeightMap terrainHeightMap) {
        int zsize = Math.round(terrainHeightMap.maxheight - terrainHeightMap.minheight + 0.5f) + 1;
        TerrainModel terrainModel = new TerrainModel(terrainHeightMap.size.x, terrainHeightMap.size.y, zsize);
        short id = terrainModel.addBlockToPalette("minecraft:stone");
        short iderror = terrainModel.addBlockToPalette("minecraft:red_wool");
        for(int i = 0; i < terrainHeightMap.size.x; i++) {
            for(int j = 0; j < terrainHeightMap.size.y; j++) {
                int z = Math.round(terrainHeightMap.getPoint(i,j) - terrainHeightMap.minheight);
                if (z < 0) terrainModel.setBlock(i,j,0, iderror);
                else terrainModel.setBlock(i,j,z, id);
            }
        }
        return terrainModel;
    }

    /**
     *  @return difference between lowest and highest point on height map
     */
    public static float zSize(TerrainHeightMap terrainHeightMap) {
        return terrainHeightMap.maxheight - terrainHeightMap.minheight;
    }
}
