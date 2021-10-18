package com.mkkl.grayscale;

import com.mkkl.types.HeightMapUtils;
import com.mkkl.types.TerrainHeightMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrayscaleMapWriter {

    BufferedImage image;
    public GrayscaleMapWriter(int sizeX, int sizeY) {
        setSize(sizeX, sizeY);
    }

    public GrayscaleMapWriter() {

    }

    public void setSize(int sizeX, int sizeY) {
        image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
    }

    public void TerrainHeighMapToImage(TerrainHeightMap terrainHeightMap, String name) throws IOException {
        setSize(terrainHeightMap.size.x, terrainHeightMap.size.y);
        float z = HeightMapUtils.zSize(terrainHeightMap);
        for(int i = 0; i < terrainHeightMap.size.x; i++) {
            for(int j = 0; j < terrainHeightMap.size.y; j++) {
                setPixel(i,j, ((terrainHeightMap.getPoint(i,j)-terrainHeightMap.minheight)/z));
            }
        }

        File outputfile = new File(name + ".bmp");
        ImageIO.write(image, "bmp", outputfile);
    }

    public void setPixel(int x, int y, float value) {
        if (value > 1) value = 1;
        if (value < 0) value = 0;
        int c = Math.round(255*value);
        image.setRGB(x, y, rgbToInt(c,c,c));
    }

    private int rgbToInt(int r, int g, int b){
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return 0xFF000000 | r | g | b;
    }
}
