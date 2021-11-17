package com.mkkl.minecraft.generation.features;

import com.mkkl.minecraft.Chunk;
import com.mkkl.types.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTreeGenerator extends TreeGenerator{

    public RandomTreeGenerator(int seed) {
        super(seed);
    }

    @Override
    public List<Vector2<Integer>> getPoints(Chunk chunk, Random random) {
        List<Vector2<Integer>> pointlist = new ArrayList<>();
        int count = random.nextInt(Math.round(getDensity()*32));
        for (int i = 0;i < count; i++) {
            pointlist.add(new Vector2<Integer>(random.nextInt(15), random.nextInt(15)));
        }
        return pointlist;
    }
}
