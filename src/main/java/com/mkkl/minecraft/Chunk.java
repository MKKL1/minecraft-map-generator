package com.mkkl.minecraft;

import com.mkkl.constants;

/**
 * @see <a href="https://minecraft.fandom.com/wiki/Chunk_format">https://minecraft.fandom.com/wiki/Chunk_format</a>
 */

public class Chunk {

    private final int[][][] blocks = new int[constants.CHUNK_HORIZONTAL_SIZE][constants.CHUNK_HORIZONTAL_SIZE][constants.CHUNK_VERTICAL_SIZE];

    private int xPos;

    private int yPos;

    private int maxHeight = 0;

    private BlockPalette blockPalette;
    public Chunk() {
        blockPalette = new BlockPalette();
        blockPalette.addBlockToPalette("minecraft:air");
    }

    public Chunk(BlockPalette blockPalette) {
        setBlockPalette(blockPalette);
    }
    /**
     * @param chunk_z height (y in minecraft)
     * @param name name of block to place e.g. minecraft:stone
     *
     * */
    public void setBlock(int chunk_x, int chunk_y, int chunk_z, String name) {
        blocks[chunk_x][chunk_y][chunk_z] = blockPalette.addBlockToPalette(name);
    }

    /**
     * @param chunk_z height (y in minecraft)
     * @param id id of block to place contained in palette
     * @see BlockPalette#addBlockToPalette(String)
     * */
    public void setBlock(int chunk_x, int chunk_y, int chunk_z, int id) {
        blocks[chunk_x][chunk_y][chunk_z] = id;
    }
    /**
     * Fills all blocks in region defined by 2 points
     * @param x1,y1,z1 First point
     * @param x2,y2,z2 Second point
     * @param name name of block to place
     * */
    public void fillBlocks(int x1, int y1, int z1, int x2, int y2, int z2, String name) {
        int id = blockPalette.getBlockPaletteId(name);
        if (id == -1)
            id = blockPalette.addBlockToPalette(name);
        fillBlocks(x1, y1, z1, x2, y2, z2, id);
    }
    /**
     * Fills all blocks in area defined by 2 points
     * @param x1,y1,z1 First point
     * @param x2,y2,z2 Second point
     * @param id id of block to place
     * @see BlockPalette#getBlockPaletteId(String)
     * */
    public void fillBlocks(int x1, int y1, int z1, int x2, int y2, int z2, int id) {
        int xlength = x1-x2+1;
        int ylength = y1-y2+1;
        int zlength = z1-z2+1;
        int xd, yd, zd;
        xd = Integer.compare(xlength, 0);
        yd = Integer.compare(ylength, 0);
        zd = Integer.compare(zlength, 0);

        int a = x1;
        int b = y1;
        int c = z1;
        for(int i = 0; i < xlength; i++) {
            for(int j = 0; j < ylength; j++) {
                for(int k = 0; k < zlength; k++) {
                    setBlock(a, b, c, id);
                    a+= xd;
                    b+= yd;
                    c+= zd;
                }
            }
        }
    }
    /**
     * Similar to {@link #fillBlocks(int, int, int, int, int, int, String)}
     * Fills column(1 block wide) with given block
     * @param x,y Coordinates of column
     * @param z1,z2 Defines start point and end point of column
     * @param name name of block to place
     * @see BlockPalette#getBlockPaletteName(int)
     * */
    public void fillColumn(int x, int y, int z1, int z2, String name) {
        int id = blockPalette.getBlockPaletteId(name);
        if (id == -1)
            id = blockPalette.addBlockToPalette(name);
        fillColumn(x, y, z1, z2, id);
    }
    /**
     * Similar to {@link #fillBlocks(int, int, int, int, int, int, int)}
     * Fills column(1 block wide) with given block
     * @param x,y Coordinates of column
     * @param z1,z2 Defines start point and end point of column
     * @param id id of block to place
     * @see BlockPalette#getBlockPaletteId(String)
     * */
    public void fillColumn(int x, int y, int z1, int z2, int id) {
        int zlength = Math.abs(z1-z2)+1;
        int zd = 1;
        if (z1>z2) zd = -1; else if (z1 == z2) zd = 0;
        int a = z1;
        for(int i = 0; i < zlength; i++) {
            setBlock(x, y, a, id);
            a += zd;
        }
    }
    /**
     * Replaces all blocks present in chunk with new given id
     * @param oldid id of block to replace
     * @param newid id of block that will replace old one
     * */
    public void replaceAll(int oldid, int newid) {
        for(int[][] a: blocks)
            for(int[] b: a)
                for(int c: b) {
                    if (c==oldid) c=newid;
                }
    }

    /**
     * @param chunk_z height (y in minecraft)
     * @return Id of block on given coordinates
     * */
    public int getBlockId(int chunk_x, int chunk_y, int chunk_z) {
        return blocks[chunk_x][chunk_y][chunk_z];
    }

    /**
     * @param chunk_z height (y in minecraft)
     * @return Name of block on given coordinates
     * */
    public String getBlockName(int chunk_x, int chunk_y, int chunk_z) {
        return blockPalette.getBlockPaletteName(getBlockId(chunk_x, chunk_z, chunk_y));
    }
    /**
     * Changes block palette by updating all blocks in chunks with new id's
     * @param newBlockPalette pallete to set in chunk
     * */
    public void applyNewBlockPalette(BlockPalette newBlockPalette) {
        if (this.blockPalette.equals(newBlockPalette)) return;
        for(String name : newBlockPalette.getBlocklist()) {
            int id = blockPalette.getBlockPaletteId(name);
            int id2 = newBlockPalette.getBlockPaletteId(name);
            if (id != id2) {
                replaceAll(id, id2);
            }
        }
    }
    /**
     * @return Block pallete of this chunk (may be different between different chunks)
     * */
    public BlockPalette getBlockPalette() {
        return blockPalette;
    }

    public void setBlockPalette(BlockPalette blockPalette) {
        this.blockPalette = blockPalette;
    }

    //From https://minecraft.fandom.com/wiki/Chunk_format
    /**
     * @return X position of the chunk (in chunks, from the origin, not relative to region)
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * @return Y position of the chunk (in chunks, from the origin, not relative to region)
     */
    public int getYPos() {
        return yPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Checks if new height value is larger than current one\n
     * If it is, {@param max} is set as new max height of chunk
     */
    public void setMaxHeight(int max) {
        if (max > maxHeight) maxHeight = max;
    }

    /**
     * Value of max height in chunk
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * @see BlockPalette#getBlockPaletteName(int)
     * @return Array of id of blocks
     */
    public int[][][] getBlocks() {
        return blocks;
    }
}
