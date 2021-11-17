package com.mkkl.io.world;

import com.mkkl.minecraft.Chunk;
import net.querz.nbt.tag.*;

import java.util.Arrays;

public class ChunkSerializer {
    IntTag DataVersion = new IntTag(2586);

    public ChunkSerializer() {

    }

    public CompoundTag getChunkNbt(Chunk chunk) {
        CompoundTag root = new CompoundTag();
        root.put("DataVersion", DataVersion);
        root.put("xPos", new IntTag(chunk.getXPos()));
        root.put("yPos", new IntTag(chunk.getYPos()));
        root.put("Status", new StringTag("features"));
        root.put("Biomes", createBiomes());
        root.put("Sections", getSectionsFromChunk(chunk));
        return root;
    }
    private IntArrayTag createBiomes() {
        IntArrayTag biomes = new IntArrayTag();
        int[] a = new int[1024];
        Arrays.fill(a, 1);
        biomes.setValue(a);
        return biomes;
    }

    private ListTag<CompoundTag> getSectionsFromChunk(Chunk chunk) {
        ListTag<CompoundTag> sections = new ListTag<>(CompoundTag.class);
        chunk.sectionCount = 0;
        sections.add(getSection(chunk, 0));
        return sections;
    }

    private CompoundTag getSection(Chunk chunk, int Y) {
        CompoundTag section = new CompoundTag();
        section.put("Y", new ByteTag((byte) Y));
        return section;
    }
}
