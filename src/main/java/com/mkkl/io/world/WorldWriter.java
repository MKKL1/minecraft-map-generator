package com.mkkl.io.world;

import com.mkkl.io.IOUTils;
import com.mkkl.minecraft.Chunk;
import com.mkkl.minecraft.Region;
import net.querz.mca.CompressionType;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class WorldWriter {

    public WorldWriter() {

    }
    //Trying to figure out how mca file is saved
    //TODO: get it working
    public void writeRegion(Region region) throws IOException {
        Chunk[][] chunkarray = region.getChunkArray();
        byte[] locations = new byte[4096];
        byte[] timestamps = new byte[4096];
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChunkSerializer chunkSerializer = new ChunkSerializer();

        for(int i = 0; i<32; i++) {
            for(int j = 0; j<32; j++) {
                if (chunkarray[i][j] != null) {
                    Chunk chunk = chunkarray[i][j];
                    CompoundTag chunkTag = chunkSerializer.getChunkNbt(chunk);
                    //Write offset
                    int offset = 4*(i + (j*32));
                    byte[] byteoffset = IOUTils.toByteArray3(offset);
                    System.arraycopy(byteoffset, 0, locations, offset, 3);
                    locations[offset+3] = 1;
                    //Write chunk data
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
                    BufferedOutputStream nbtOut = new BufferedOutputStream(CompressionType.ZLIB.compress(baos));
                    new NBTSerializer(false).toStream(new NamedTag(null, chunkTag), nbtOut);
                    out.write(baos.toByteArray());

                }
            }
        }

        byte[] chunkdata = out.toByteArray();
        FileOutputStream outputStream = new FileOutputStream("r.0.0.mca");
        outputStream.write(locations);
        outputStream.write(timestamps);
        outputStream.write(chunkdata);
        outputStream.close();
    }
}
