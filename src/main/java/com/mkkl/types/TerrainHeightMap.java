package com.mkkl.types;

import com.mkkl.ScalingAlgorithms;

public class TerrainHeightMap {
    private float[][] heightmap;

    /**
     * Size of array that stores data points
     * */
    public Vector2<Integer> size = new Vector2<Integer>(); //Size of array
    /**
     * Size of real world data sample taken in meters
     * */
    public Vector2<Float> realsize = new Vector2<Float>(); //Size of model in meters
    /**
     * Sometimes also called accuracy
     * <p><p/>
     * Distance between each data point on grid or height map in meters
     * */
    private Vector2<Float> resolution = new Vector2<Float>(); //Distance in meters between each sample

    public float maxheight = 0;
    public float minheight = Float.MAX_VALUE;

    public TerrainHeightMap(int sizeX, int sizeY) {
        this.size.x = sizeX;
        this.size.y = sizeY;
        setHeightmap(new float[sizeX][sizeY]);
    }

    public TerrainHeightMap(float[][] _heightmap) {
        this.size.x = _heightmap.length;
        this.size.y = _heightmap[0].length;
        setHeightmap(_heightmap);
        recalculateExtremes();
    }

    public TerrainHeightMap(Vector2<Integer> size) {
        this(size.x, size.y);
    }

    public void setPoint(int x, int y, float value) {
        if (value > maxheight) maxheight = value;
        else if (value < minheight) minheight = value;
        heightmap[x][y] = value;
    }

    public float getPoint(int x, int y) {
        return heightmap[x][y];
    }

    /**
     * @inheritDoc this#resolution
     * */
    public void setResolution(float xdistance, float ydistance) {
        resolution.x = xdistance;
        resolution.y = ydistance;
        realsize.x = resolution.x * size.x;
        realsize.y = resolution.y * size.y;
    }

    /**
     * @inheritDoc this#resolution
     * */
    public void setResolution(Vector2<Float> _resolution) {
        setResolution(_resolution.x, _resolution.y);
    }

    /**
     * @inheritDoc this#resolution
     * */
    public Vector2<Float> getResolution() {
        return resolution;
    }

    /**
     * @see #recalculateExtremes()
     */
    private void setHeightmap(float[][] heightmap) {
        this.heightmap = heightmap;
    }

    public float[][] getHeightmap() {
        return heightmap;
    }

    /**
     * Calculates lowest and highest point on height map
     * @see #heightmap
     */
    public void recalculateExtremes() {
        maxheight = 0;
        minheight = Float.MAX_VALUE;
        float value;
        for(int x = 0; x < size.x; x++)
            for(int y = 0; y < size.y; y++) {
                value = getPoint(x, y);
                if (value > maxheight) maxheight = value;
                else if (value < minheight) minheight = value;
            }
    }

    /**
     * Changes real world distance between each data point (meters)
     * It will change {@link this#size}
     * */
    public TerrainHeightMap scaleToResolution(Vector2<Float> newresolution) {
        float scalefactorX = (this.getResolution().x/newresolution.x);
        float scalefactorY = (this.getResolution().y/newresolution.y);
        TerrainHeightMap scaledmap;
        if(scalefactorX > 1) {
            scaledmap = new TerrainHeightMap(ScalingAlgorithms.bilinearInterpolation(this.getHeightmap(), scalefactorX, scalefactorY));
        } else {
            scaledmap = new TerrainHeightMap(ScalingAlgorithms.nearestNeighbor(this.getHeightmap(), scalefactorX, scalefactorY));
        }
        scaledmap.setResolution(newresolution);
        return scaledmap;
    }

    /**
     * Scales real world distance between each data point
     * @param scalefactor e.g. {@code scalefactor = new Vector2<Float>(2f, 2f);} would change {@link TerrainHeightMap#resolution} of each x and y from 1 to 0.5
     * It will change {@link this#size}
     */
    public TerrainHeightMap scaleMap(Vector2<Float> scalefactor) {
        return scaleToResolution(new Vector2<>(this.getResolution().x/scalefactor.x, this.getResolution().y/scalefactor.y));
    }

    /**
     * Scales height map.
     * That includes height as well.
     * @param scalefactor similar to {@link #scaleMap(Vector2)} but with z axis included
     * */
    public TerrainHeightMap scaleMap(Vector3<Float> scalefactor) {
        Vector2<Float> newresolution = new Vector2<>(this.getResolution().x/scalefactor.x, this.getResolution().y/scalefactor.y);
        TerrainHeightMap newmap = this.scaleToResolution(newresolution);

        for(int x = 0; x < newmap.size.x; x++) {
            for(int y = 0; y < newmap.size.y; y++) {
                newmap.setPoint(x,y,newmap.getPoint(x,y)*scalefactor.z);
            }
        }
        newmap.recalculateExtremes();
        return newmap;
    }

    /**
     *  @return difference between lowest and highest point on height map
     */
    public float zSize() {
        return this.maxheight - this.minheight;
    }

}
