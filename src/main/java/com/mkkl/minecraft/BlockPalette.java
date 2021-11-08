package com.mkkl.minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockPalette {
    private final List<String> blocklist = new ArrayList<String>();
    private int listiterator = -1;
    /**
     * @return id of added block or already existing with same name
     */
    public int addBlockToPalette(String name) {
        if (!blocklist.contains(name)) {
            listiterator++;
            blocklist.add(name);
            return listiterator;
        } else return getBlockPaletteId(name);
    }

    /**
     * @return id of block stored in palette
     * @see List#indexOf(Object)
     */
    public int getBlockPaletteId(String name) {
        return blocklist.indexOf(name);
    }

    /**
     * @return name of block stored in palette ex. "minecraft:stone"
     */
    public String getBlockPaletteName(int id) {
        return blocklist.get(id);
    }

    public void removeBlock(int id) {
        blocklist.remove(id);
    }

    public void removeBlock(String name) {
        removeBlock(getBlockPaletteId(name));
    }

    public int size() {
        return blocklist.size();
    }

    public boolean equals(BlockPalette otherBlockPalette) {
        return blocklist.equals(otherBlockPalette.blocklist);
    }

    public boolean contains(String name) {
        return blocklist.contains(name);
    }

    /**
     * @return List of different blocks between two palettes
     */
    public List<String> difference(BlockPalette otherBlockPalette) {
        return this.blocklist.stream()
                .filter(x -> ! otherBlockPalette.blocklist.contains(x))
                .collect(Collectors.toList());
    }

    public List<String> getBlocklist() {
        return blocklist;
    }
}
