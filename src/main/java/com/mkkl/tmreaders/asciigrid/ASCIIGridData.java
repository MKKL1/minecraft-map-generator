package com.mkkl.tmreaders.asciigrid;

import com.mkkl.tmreaders.ParsingException;
import com.mkkl.tmreaders.TerrainHeightMapReader;
import com.mkkl.types.TerrainHeightMap;

import java.io.IOException;
import java.util.HashMap;

public class ASCIIGridData implements TerrainHeightMapReader {
    private HashMap<String, String> attributes = new HashMap<String, String>();
    private float[][] grid;
    public int columns;
    public int rows;

    public boolean containsAttr(String key) {
        return attributes.containsKey(key);
    }

    public void addAttr(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttr(String key) throws ParsingException {
        if (!containsAttr(key)) throw new ParsingException("Attribute '" + key + "' not found");
        return attributes.get(key);
    }

    public int getAttrInt(String key) throws ParsingException {
        return Integer.parseInt(getAttr(key));
    }

    public float getAttrFloat(String key) throws ParsingException {
        return Float.parseFloat(getAttr(key));
    }

    public double getAttrDouble(String key) throws ParsingException {
        return Double.parseDouble(getAttr(key));
    }

    public void setGridPoint(int x, int y, float v) {
        grid[x][y] = v;
    }

    public void setGridSize(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        grid = new float[columns][rows];
    }

    @Override
    public TerrainHeightMap toTerrainHeightMap() throws IOException {
        TerrainHeightMap terrainHeightMap = new TerrainHeightMap(grid);

        if (containsAttr("dx") && containsAttr("dy")) {
            terrainHeightMap.setResolution(getAttrFloat("dx"), getAttrFloat("dy"));
        } else if(containsAttr("cellsize")){
            float size = getAttrFloat("cellsize");
            terrainHeightMap.setResolution(size, size);
        }

        return terrainHeightMap;
    }
}
