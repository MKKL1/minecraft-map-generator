package com.mkkl.minecraft.generation.features;

import com.mkkl.minecraft.Chunk;
import com.mkkl.types.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JitteredGridGenerator extends TreeGenerator{

    @Override
    public List<Vector2<Integer>> getPoints(Chunk chunk, Random random) {
        List<Vector2<Integer>> pointlist = new ArrayList<>();
        float density = 0.4f;
        int split = Math.round(1/density);
        for(int i=0; i < 16; i+= split) {
            for(int j=0; j<16; j+= split) {

            }
        }
        return null;
    }
}
