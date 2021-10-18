package com.mkkl.minecraft;

import com.mkkl.types.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class TerrainModel {
    private short[][][] terraindata;
    private final List<String> palette = new ArrayList<>();
    private Vector3<Integer> size;
    private short listiterator = -1;

    /**
     * Used to store terrain data in minecraft block form
     * Using cartesian coordinate system (Z is height)
     *
     * @param   Xsize maximum size of terrain in x axis
     * @param   Ysize ... y axis (z in minecraft)
     * @param   Zsize ... z axis (y in minecraft)
     */
    public TerrainModel(int Xsize, int Ysize, int Zsize) {
        terraindata = new short[Xsize][Ysize][Zsize];
        addBlockToPalette("minecraft:air");
        size = new Vector3<>(Xsize, Ysize, Zsize);
    }

    public TerrainModel(Vector3<Integer> size) {
        this(size.x, size.y, size.z);
    }

    /**
     * @return id of added block or already existing with same name
     */
    public short addBlockToPalette(String name) {
        if (!palette.contains(name)) {
            palette.add(name);
            listiterator++;
            return listiterator;
        } else return getBlockPaletteId(name);

    }

    /**
     * For better performence use {@link #setBlock(int, int, int, short)}
     */
    public void setBlock(int x, int y, int z, String name) {
        int index = palette.indexOf(name);
        if (index == -1) {
            addBlockToPalette(name);
            terraindata[x][y][z] = (short)palette.indexOf(name);
        } else {
            terraindata[x][y][z] = (short)index;
        }
    }

    /**
     * @return id of block stored in palette
     */
    public short getBlockPaletteId(String name) {
        return (short)palette.indexOf(name);
    }

    /**
     * Uses id of already added block to palette
     * @see #addBlockToPalette(String)
     */
    public void setBlock(int x, int y, int z, short id) {
        terraindata[x][y][z] = id;
    }


    public short[][][] getTerraindata() {
        return terraindata;
    }

    public void setTerraindata(short[][][] terraindata) {
        this.terraindata = terraindata;
    }

    public List<String> getPalette() {
        return palette;
    }

    public Vector3<Integer> getSize() {
        return size;
    }
}
