package engine.terrain;

import org.lwjgl.Sys;

public class WorldTerrain {

    private Terrain[][] terrainMap;
    private int width;
    private int length;

    public WorldTerrain(int width,int length){
        terrainMap = new Terrain[width][length];
        this.width = width;
        this.length = length;
    }

    public void setParcel(Terrain terrain,int gridX,int gridZ){
        if(gridX >= width || gridZ >= length){
            System.err.println("Parcel index out of bounds");
        }
        terrainMap[gridX][gridZ] = terrain;
    }

    public Terrain getParcel(float posX,float posZ){
        float parcelSize = Terrain.SIZE;
        int gridX = (int) (posX/parcelSize);
        int gridZ = (int) (posZ/parcelSize);
        if(gridX >= width || gridZ >= length){
            return null;
        }
        return terrainMap[gridX][gridZ];
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public Terrain[][] getTerrainMap() {
        return terrainMap;
    }
}
