package com.mkkl.minecraft;

import com.mkkl.constants;

import java.util.HashMap;
import java.util.Map;

public class Chunk {

    private final short[][][] blocks = new short[constants.CHUNK_HORIZONTAL_SIZE][constants.CHUNK_HORIZONTAL_SIZE][constants.CHUNK_VERTICAL_SIZE];
    private final HashMap<Short, String> palette = new HashMap<Short, String>();
    private short palette_enumerator = 0;
    public Chunk() {
        addBlockToPalette("minecraft:air");
    }
    /**
     * @param chunk_z height (y in minecraft)
     * @param name name of block to place e.g. minecraft:stone
     *
     * */
    public void setBlock(int chunk_x, int chunk_z, int chunk_y, String name) {
        blocks[chunk_x][chunk_y][chunk_z] = addBlockToPalette(name);
    }

    /**
     * @param chunk_z height (y in minecraft)
     * @param id id of block to place contained in palette
     * @see #addBlockToPalette(String)
     * */
    public void setBlock(int chunk_x, int chunk_z, int chunk_y, short id) {
        blocks[chunk_x][chunk_y][chunk_z] = id;
    }

    /**
     * @param chunk_z height (y in minecraft)
     * */
    public short getBlockId(int chunk_x, int chunk_z, int chunk_y) {
        return blocks[chunk_x][chunk_y][chunk_z];
    }

    /**
     * @return -1 if block wasn't found
     * */
    public short getBlockIdFromPalette(String name) {
        for (Map.Entry<Short, String> entry : palette.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }
    /**
     * @return id of added or existing block
     * @see #getBlockIdFromPalette(String)
     * */
    public short addBlockToPalette(String name) {
        short tmp;
        if (palette.containsValue(name)) {
            tmp = getBlockIdFromPalette(name);
            if (tmp == -1) throw new IllegalArgumentException("Hashmap palette doesn't contain " + name);
        } else {
            palette.put(palette_enumerator, name);
            tmp = palette_enumerator;
            palette_enumerator++;
        }
        return tmp;
    }
}
