package engine.terrain;


import animation.animation.Quaternion;
import colladaParser.dataStructures.MeshData;
import engine.openglObjects.Vao;
import engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    public static final float SIZE = 300;
    private static final int MAX_HEIGHT = 20;
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    private final float x;
    private final float z;
    private final Vao model;

  //  private TerrainTexturePack texturePack;
  //  private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, String heightMap) {
  //      this.texturePack = texturePack;
  //      this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(heightMap);
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return -700;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;
        if (xCoord <= (1 - zCoord)) {
            answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    private Vao generateTerrain(String heightMap) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        MeshData meshData = new MeshData(vertices,textureCoords,normals,indices,null,null);
        return createVao(meshData);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Vao getModel() {
        return model;
    }

    public Matrix4f getTransformationMatrix() {
        return Maths.createTransformationMatrix(new Vector3f(x,0,z),new Quaternion(0,0,0,1),1);
    }
 /*   public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
*/
    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOUR / 2f;
        height /= MAX_PIXEL_COLOUR / 2f;
        height *= MAX_HEIGHT;
        return height;
    }

    private static Vao createVao(MeshData data) {
        Vao vao = Vao.create();
        vao.bind();
        vao.createIndexBuffer(data.getIndices());
        vao.createAttribute(0, data.getVertices(), 3);
        vao.createAttribute(1, data.getTextureCoords(), 2);
        vao.createAttribute(2, data.getNormals(), 3);
    //    vao.createIntAttribute(3, data.getJointIds(), 3);
    //    vao.createAttribute(4, data.getVertexWeights(), 3);
        vao.unbind();
        return vao;
    }
}
