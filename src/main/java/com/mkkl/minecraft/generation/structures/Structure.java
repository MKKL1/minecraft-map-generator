package com.mkkl.minecraft.generation.structures;

import com.mkkl.minecraft.BlockPalette;

public class Structure {
    public int xSize;
    public int ySize;
    public int zSize;
    public BlockPalette blockPalette = new BlockPalette();
    public short[][][] blocks;
    public Structure(int xSize,int ySize,int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        blocks = new short[xSize][ySize][zSize];
        blockPalette.addBlockToPalette("null");
    }
}
