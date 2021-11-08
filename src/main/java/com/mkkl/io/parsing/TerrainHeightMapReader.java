package com.mkkl.io.parsing;

import com.mkkl.types.TerrainHeightMap;

import java.io.IOException;

public interface TerrainHeightMapReader {
    TerrainHeightMap toTerrainHeightMap() throws IOException;
    int getXSize();
    int getYSize();
}
