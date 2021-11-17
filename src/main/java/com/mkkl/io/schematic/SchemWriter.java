package com.mkkl.io.schematic;

import com.mkkl.minecraft.BlockPalette;
import com.mkkl.minecraft.TerrainModel;
import com.mkkl.types.Vector3;
import net.querz.nbt.io.*;
import net.querz.nbt.tag.*;

import java.io.*;

public class SchemWriter {

    private File file;
    private CompoundTag root;
    public SchemWriter(File file) {
        this.file = file;
        root = new CompoundTag();
    }

    public SchemWriter(String path){
        this(new File(path));
    }

    public void write(TerrainModel terrainModel) throws IOException {
        write(new SchematicData(terrainModel));
    }
    public void write(SchematicData schematicData) throws IOException {
        root.putInt("Version", 2);
        root.putInt("DataVersion", 2586);
        root.put("BlockEntities", new ListTag<>(CompoundTag.class));//Empty
        writePalette(schematicData.getBlockPalette());
        writeMetadata(schematicData.weoffset);
        writeBlockData(schematicData);
        writeOffset(schematicData.offset);
    }
    private void writeBlockData(SchematicData schematicData) throws IOException {
        root.putShort("Width", schematicData.width);
        root.putShort("Length", schematicData.length);
        root.putShort("Height", schematicData.height);

        ByteArrayTag byteArrayTag = new ByteArrayTag();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        short[][][] heightmap = schematicData.getBlocks();
        for(int z = 0; z < schematicData.height; z++) {
            for(int y = 0; y < schematicData.length; y++) {
                for(int x = 0; x < schematicData.width; x++) {
                    byteArrayOutputStream.write(heightmap[x][y][z]);
                }
            }
        }
        byteArrayTag.setValue(byteArrayOutputStream.toByteArray());
        root.put("BlockData", byteArrayTag);
        byteArrayOutputStream.close();
    }

    private void writeMetadata(Vector3<Integer> weoffset) {
        CompoundTag metadataTag = new CompoundTag();
        metadataTag.putInt("WEOffsetX", weoffset.x);
        metadataTag.putInt("WEOffsetY", weoffset.z);//Minecraft is using different coordinate system (z and y are swapped)
        metadataTag.putInt("WEOffsetZ", weoffset.y);
        root.put("Metadata", metadataTag);
    }

    private void writePalette(BlockPalette blockPalette) {
        int palettemax = blockPalette.size();
        CompoundTag paletteTag = new CompoundTag();
        for (int i = 0; i < palettemax; i++) {
            paletteTag.putInt(blockPalette.getBlockPaletteName(i), i);
        }

        root.putInt("PaletteMax", palettemax);

        root.put("Palette", paletteTag);
    }

    private void writeOffset(Vector3<Integer> offset) {
        IntArrayTag intArrayTag = new IntArrayTag();
        int[] buffer = new int[3];
                             // mc coordinate system - cartesian coordinate system
        buffer[0] = offset.x;// x - x
        buffer[1] = offset.z;// z - y
        buffer[2] = offset.y;// y - z
        intArrayTag.setValue(buffer);
        root.put("Offset", intArrayTag);
    }

    /**
     *  Compresses and saves root tag to file
     * @param compressed Whether or not compress schem file
     * @see #save()
     */
    //NBTUtil#write doesn't want to write uncompressed files
    public void save(boolean compressed) throws IOException {
        NamedTag namedTag = new NamedTag("Schematic", root);
        NBTUtil.write(namedTag, file, compressed);
    }

    /**
     *  Compresses and saves root tag to file
     * @see #save(boolean)
     */
    public void save() throws IOException {
        save(true);
    }

    public CompoundTag getRoot() {
        return root;
    }

    public void writeTag(String key, Tag tag) {
        root.put(key,tag);
    }
}
