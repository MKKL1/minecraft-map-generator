package com.mkkl.minecraft;

import com.mkkl.types.Vector3;

import java.util.Collection;
import java.util.List;


public class TerrainModel {
    private Chunk[][] terraindata;
    private Vector3<Integer> size;

    /**
     * Used to store terrain data in minecraft block form
     * Using cartesian coordinate system (Z is height)
     *
     * //@param   Xsize maximum size of terrain in x axis
     * //@param   Ysize ... y axis (z in minecraft)
     * //@param   Zsize ... z axis (y in minecraft)
     */
    public TerrainModel(Chunk[][] blocks, Vector3<Integer> size) {
        terraindata = blocks;
        setSize(size);
    }

    public TerrainModel(TerrainManager terrainManager, Vector3<Integer> size) {
        terraindata = new Chunk[terrainManager.chunkSize.x][terrainManager.chunkSize.y];
        for (Region region: terrainManager.getRegionList().values()) {
            Chunk[][] chunks = region.getChunkArray();
            int x = region.xPos;
            int y = region.yPos;
            for(int j = 0; j < chunks.length; j++) {
                for(int k = 0; k < chunks[j].length; k++) {
                    Chunk chunk = chunks[j][k];
                    if (chunk != null)
                        terraindata[(x*32)+j][(y*32)+k] = chunk;
                }
            }
        }
        setSize(size);
    }

    /**
     * For better performence use {@link #setBlock(int, int, int, int)}
     */
    public void setBlock(int x, int y, int z, String name) {
        terraindata[x/16][y/16].setBlock(x%16, y%16,z, name);
    }



    /**
     * Uses id of already added block to palette
     * @see BlockPalette#addBlockToPalette(String) 
     */
    public void setBlock(int x, int y, int z, int id) {
        terraindata[x/16][y/16].setBlock(x%16, y%16,z, id);
    }

    public void fillBlocks(int x1, int y1, int z1, int x2, int y2, int z2, int id) {
        int xlength = x1-x2;
        int ylength = y1-y2;
        int zlength = z1-z2;
        int xd = Integer.compare(0, xlength);
        int yd = Integer.compare(0, ylength);
        int zd = Integer.compare(0, zlength);

        for(int i = 0; i < xlength; i++) {
            for(int j = 0; j < ylength; j++) {
                for(int k = 0; k < zlength; k++) {
                    setBlock(x1+xd, y1+yd, z1+zd, id);
                }
            }
        }
    }

    public Vector3<Integer> getSize() {
        return size;
    }

    public void setSize(Vector3<Integer> size) {
        this.size = size;
    }

    /**
     * Finds all blocks in chunks and stores them in one {@link BlockPalette}
     * @see this#applyNewBlockPaletteToAll(BlockPalette)
     */
    public BlockPalette getCombinedBlockPalette() {
        BlockPalette blockPalette = new BlockPalette();
        for(Chunk[] a: terraindata)
            for(Chunk b: a) {
                if (b != null)
                blockPalette.getBlocklist().addAll(b.getBlockPalette().difference(blockPalette));
            }
        return blockPalette;
    }

    /**
     * Applies block palette to all stored chunks
     * @see Chunk#applyNewBlockPalette(BlockPalette)
     */
    public void applyNewBlockPaletteToAll(BlockPalette newBlockPalette) {
        for(Chunk[] a: terraindata)
            for(Chunk b: a) {
                if (b != null)
                b.applyNewBlockPalette(newBlockPalette);
            }
    }

    /**
     * @return Array of chunks
     */
    public Chunk[][] getTerraindata() {
        return terraindata;
    }
}
