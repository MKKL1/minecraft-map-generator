package com.mkkl.minecraft.generation.features;

import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.generation.structures.Structure;
import com.mkkl.types.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeGenerator {
    private float density = 0.5f;
    public long seed;
    public List<Structure> trees = new ArrayList<>();
    public TreeGenerator(long seed) {
        this.seed = seed;
    }
    public TreeGenerator() {
        this((int)System.currentTimeMillis());
    }

    public void setDensity(float density) {
        if (density > 1) density = 1;
        else if (density < 0) density = 0;
        this.density = density;
    }

    public float getDensity() {
        return density;
    }

    public List<Vector2<Integer>> getPoints(Chunk chunk, Random random) {
        return null;
    }
}
