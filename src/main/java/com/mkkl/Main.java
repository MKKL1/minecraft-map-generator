package com.mkkl;

import com.mkkl.minecraft.TerrainModel;
import com.mkkl.io.schematic.SchemWriter;
import com.mkkl.io.parsing.asciigrid.ASCIIGridParser;
import com.mkkl.minecraft.generation.GenerateWorldData;
import com.mkkl.minecraft.generation.TerrainInfill;
import com.mkkl.types.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {

        try {

            long startTime = System.nanoTime();
            TerrainHeightMap terrainHeightMap = ASCIIGridParser.parse("result.asc").toTerrainHeightMap();

            TerrainModel terrainModel = new GenerateWorldData(
                    //Making it smaller because pasting large schematic with worldedit takes too much time
                    //Would be nice to have mcedit back
                    terrainHeightMap.scaleToResolution(new Vector2<Float>(2f, 2f)))
                    .setInfillType(TerrainInfill.FULL)
                    .generate().getTerrainModel();

            SchemWriter schemWriter = new SchemWriter("test.schem");
            schemWriter.write(terrainModel);
            schemWriter.save(true);

            long stopTime = System.nanoTime();
            System.out.println((double)(stopTime - startTime)/1000000000);

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
