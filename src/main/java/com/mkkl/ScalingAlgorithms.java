package com.mkkl;

public class ScalingAlgorithms {

    public static float[][] bilinearInterpolation(float[][] map, float scalingfactorX, float scalingfactorY) {
        int mapsizeX = map.length;
        int mapsizeY = map[0].length;

        int newsizeX = Math.round(mapsizeX * scalingfactorX);
        int newsizeY = Math.round(mapsizeY * scalingfactorY);
        float[][] newmap = new float[newsizeX][newsizeY];

        for(int x = 0; x < newsizeX; x++) {
            for(int y = 0; y < newsizeY; y++) {
                float samplex = ((float)x / newsizeX) * (mapsizeX-1);
                float sampley = ((float)y / newsizeY) * (mapsizeY-1);
                int samplexint = (int) samplex;
                int sampleyint = (int) sampley;
                float v = lerp(lerp(map[samplexint][sampleyint], map[samplexint+1][sampleyint], samplex-samplexint),
                        lerp(map[samplexint][sampleyint+1], map[samplexint+1][sampleyint+1], samplex-samplexint),
                        sampley-sampleyint);
                newmap[x][y] = v;
            }
        }
        return newmap;
    }

    public static float[][] nearestNeighbor(float[][] map, float scalingfactorX, float scalingfactorY) {
        int mapsizeX = map.length;
        int mapsizeY = map[0].length;

        int newsizeX = Math.round(mapsizeX * scalingfactorX);
        int newsizeY = Math.round(mapsizeY * scalingfactorY);

        float[][] newmap = new float[newsizeX][newsizeY];

        for(int x = 0; x < newsizeX; x++) {
            for(int y = 0; y < newsizeY; y++) {
                newmap[x][y] = map[Math.round(((float)x / newsizeX) * (mapsizeX-1))][Math.round(((float)y / newsizeY) * (mapsizeY-1))];
            }
        }
        return newmap;
    }

    private static float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
}
