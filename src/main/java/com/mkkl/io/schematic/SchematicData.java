package com.mkkl.io.schematic;

import com.mkkl.constants;
import com.mkkl.minecraft.BlockPalette;
import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.TerrainModel;
import com.mkkl.types.Vector3;

public class SchematicData {
    public Vector3<Integer> weoffset;
    public Vector3<Integer> offset;
    /**
     * Length of terrain on x axis
     */
    public short width;
    /**
     * Length of terrain on y axis
     */
    public short length;
    /**
     * Length of terrain on z axis
     */
    public short height;
    private short[][][] blocks;

    private BlockPalette blockPalette;

    public int predictedFileSizeMb = 0;

    private boolean _ignoreSizeLimit;

    /**
     * @param size Size of terrain model
     */
    public void setDimensions(Vector3<Integer> size) {
        width = size.x.shortValue();
        length = size.y.shortValue();
        height = size.z.shortValue();
        //Blocks are saved in .schem file as byte corresponding to name of block in block palette
        predictedFileSizeMb = (int)(((long)width*length*height)/1000000);//In mb
        if (predictedFileSizeMb > constants.SCHEM_MAX_SIZE_MB && !_ignoreSizeLimit)
            throw new OutOfMemoryError("Schem file shouldn't be larger than " + constants.SCHEM_MAX_SIZE_MB + " mb");
    }

    public SchematicData(TerrainModel terrainModel) {
        offset = new Vector3<>(0,0,0);
        weoffset = new Vector3<>(0,0,0);
        setTerrainModel(terrainModel);
    }

    public void setTerrainModel(TerrainModel terrainModel) {
        setDimensions(terrainModel.getSize());
        blockPalette = terrainModel.getCombinedBlockPalette();
        terrainModel.applyNewBlockPaletteToAll(blockPalette);
        Vector3<Integer> size = terrainModel.getSize();
        blocks = new short[size.x][size.y][size.z];
        Chunk[][] chunks = terrainModel.getTerraindata();
        for(int i = 0; i < chunks.length; i++) {
            for(int j = 0; j < chunks[0].length; j++) {
                for(int k = 0; k < 16; k++) {
                    for(int l = 0; l < 16; l++) {
                        for(int m = 0; m < size.z; m++) {
                            blocks[(i*16)+k][(j*16)+l][m] = chunks[i][j].getBlock(k,l,m);
                    }}}

            }
        }
    }

    public short[][][] getBlocks() {
        return blocks;
    }

    public BlockPalette getBlockPalette() {
        return blockPalette;
    }

    public void ignoreFileSizeLimit() {
        _ignoreSizeLimit = true;
    }
}
