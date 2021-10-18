package com.mkkl.schematic;

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
    private TerrainModel terrainModel;

    /**
     * @param size Size of terrain model
     */
    public void setDimensions(Vector3<Integer> size) {
        width = size.x.shortValue();
        length = size.y.shortValue();
        height = size.z.shortValue();
    }

    public SchematicData() {
        offset = new Vector3<>(0,0,0);
        weoffset = new Vector3<>(0,0,0);
    }

    public SchematicData(TerrainModel terrainModel) {
        this();
        setTerrainModel(terrainModel);
    }

    public void setTerrainModel(TerrainModel terrainModel) {
        this.terrainModel = terrainModel;
        setDimensions(terrainModel.getSize());
    }

    public TerrainModel getTerrainModel() {
        return terrainModel;
    }
}
