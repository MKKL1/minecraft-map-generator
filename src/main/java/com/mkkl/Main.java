package com.mkkl;

import com.mkkl.minecraft.TerrainModel;
import com.mkkl.schematic.SchemWriter;
import com.mkkl.schematic.SchematicData;
import com.mkkl.tmreaders.asciigrid.ASCIIGridParser;
import com.mkkl.types.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {

            long startTime = System.nanoTime();
            TerrainHeightMap terrainHeightMap = ASCIIGridParser.parse("D:\\pobieranie\\result(7).asc").toTerrainHeightMap();
            long stopTime = System.nanoTime();
            System.out.println((double)(stopTime - startTime)/1000000000);


            TerrainModel terrainModel = HeightMapUtils.heightMapToMcModel(HeightMapUtils.scaleToResolution(terrainHeightMap, new Vector2<Float>(1f, 1f)));
            SchematicData schematicData = new SchematicData(terrainModel);

            SchemWriter schemWriter = new SchemWriter("C:\\Users\\mkkl\\Desktop\\test2.schem");
            schemWriter.write(schematicData);
            schemWriter.save(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
