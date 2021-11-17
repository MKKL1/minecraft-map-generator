package com.mkkl;

import com.mkkl.io.grayscale.GrayscaleMapWriter;
import com.mkkl.io.schematic.SchematicData;
import com.mkkl.io.world.WorldWriter;
import com.mkkl.minecraft.TerrainManager;
import com.mkkl.minecraft.TerrainModel;
import com.mkkl.io.schematic.SchemWriter;
import com.mkkl.io.parsing.asciigrid.ASCIIGridParser;
import com.mkkl.minecraft.generation.GenerateWorldData;
import com.mkkl.minecraft.generation.TerrainInfill;
import com.mkkl.minecraft.generation.features.JitteredGridGenerator;
import com.mkkl.minecraft.generation.features.RandomTreeGenerator;
import com.mkkl.minecraft.generation.features.TreeGenerator;
import com.mkkl.minecraft.generation.structures.Structure;
import com.mkkl.types.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );
    public static void main(String[] args) {

        try {
            System.setProperty("java.util.logging.SimpleFormatter.format","[%1$tF %1$tT] [%4$-7s] %5$s %n");
            long startTime = System.nanoTime();
            TerrainHeightMap terrainHeightMap = ASCIIGridParser.parse("result.asc").toTerrainHeightMap();

            TreeGenerator treeGenerator = new RandomTreeGenerator(69);
            treeGenerator.setDensity(0.1f);

            //Will probably change it to be more flexible
            Structure tree1 = new Structure(5,5,7);
            tree1.blockPalette.addBlockToPalette("minecraft:air");//1
            tree1.blockPalette.addBlockToPalette("minecraft:oak_log");//2
            tree1.blockPalette.addBlockToPalette("minecraft:oak_leaves");//3
            tree1.blocks = new short[][][]{
                    {{0, 0, 0, 2, 2, 0, 0}, {0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 0, 0, 0}},
                    {{0, 0, 0, 2, 2, 0, 0}, {0, 0, 0, 2, 2, 2, 0},{0, 0, 0, 2, 2, 2, 2},{0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0}},
                    {{0, 0, 0, 2, 2, 0, 0}, {0, 0, 0, 2, 2, 2, 2},{1, 1, 1, 1, 1, 1, 2},{0, 0, 0, 2, 2, 2, 2},{0, 0, 0, 2, 2, 0, 0}},
                    {{0, 0, 0, 2, 2, 0, 0}, {0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 2, 2},{0, 0, 0, 2, 2, 2, 0},{0, 0, 0, 2, 2, 0, 0}},
                    {{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0},{0, 0, 0, 2, 2, 0, 0}},
            };
            treeGenerator.trees.add(tree1);

            TerrainManager terrainModel = new GenerateWorldData(
                    //Making it smaller because pasting large schematic with worldedit takes too much time
                    terrainHeightMap.scaleToResolution(new Vector2<Float>(4f, 4f)))
                    .setInfillType(TerrainInfill.FULL)
                    .setTreeGenerator(treeGenerator)
                    .setLogger(LOGGER)
                    .setThreadCount(8)
                    .generateTerrainManager();

            // Using this method for now, when I get it working it should do it in world generator to unload regions
            // from memory instead of just removing it
            WorldWriter worldWriter = new WorldWriter();
            worldWriter.writeRegion(terrainModel.getRegion(0,0));

            //SchemWriter schemWriter = new SchemWriter("test.schem");
            //SchematicData schematicData = new SchematicData(terrainModel);
            //LOGGER.log(Level.INFO, "Schematic size: {0} mb", schematicData.predictedFileSizeMb);
            //schemWriter.write(schematicData);
            //schemWriter.save(true);

            //GrayscaleMapWriter grayscaleMapWriter = new GrayscaleMapWriter();
            //grayscaleMapWriter.schematicDataToImage(schematicData, "c");

            long stopTime = System.nanoTime();
            System.out.println((double)(stopTime - startTime)/1000000000);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
