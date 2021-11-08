package com.mkkl.minecraft.generation;

public enum TerrainInfill {
    /**
     * Generates only one block on top
     */
    HOLLOW,
    /**
     * Similar to HOLLOW, but covers holes that may appear from more than 2 block difference
     * @See HOLLOW
     */
    COVERED,
    /**
     * Generates blocks from top to bottom
     */
    FULL
}
