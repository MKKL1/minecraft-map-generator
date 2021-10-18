package com.mkkl.tmreaders;

import com.mkkl.types.TerrainHeightMap;

import java.io.IOException;

public interface TerrainHeightMapReader {
    TerrainHeightMap toTerrainHeightMap() throws IOException;
}
