package shapes;

import animation.animation.Quaternion;
import colladaParser.dataStructures.MeshData;
import engine.openglObjects.Vao;
import engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Shape {

  private final Vao model;

  protected Vector3f position;
  protected Vector3f rotation;
  protected float scale;
  protected Vector4f colour;

  public static final Vector4f BLUE = new Vector4f(0, 0.2f, 0.5f, 1.0f);
  public static final Vector4f YELLOW = new Vector4f(1f, 0.5f, 0f, 1.0f);

  public Shape(float scale, Vector4f colour) {
    this.model = generateShape();
    position = new Vector3f(0, 0, 0);
    rotation = new Vector3f(0, 0, 0);
    this.scale = scale;
    this.colour = colour;
  }

  public Shape(float scale) {
    this(scale, BLUE);
  }

  private Vao generateShape() {
    float[] vertices = {
        -1, -1, -1, // BACK BOT LEFT 0
        1, -1, -1, // BACK BOT RIGHT 1
        -1, 1, -1, // BACK TOP LEFT 2
        1, 1, -1, // BACK TOP RIGHT 3
        -1, 1, 1, // FRONT TOP LEFT 4
        1, -1, 1, // FRONT BOT RIGHT 5
        1, 1, 1, // FRONT TOP RIGHT 6
        -1, -1, 1 // FRONT BOT LEFT 7
    };
    float[] normals = new float[6 * 3];

    int[] indices = {
        0, 1, 2,
        2, 0, 3,
        3, 1, 6,
        6, 1, 5,
        1, 5, 7,
        7, 5, 4,
        7, 4, 0,
        4, 2, 0,
        4, 2, 3,
        4, 3, 6
    };

    MeshData meshData = new MeshData(vertices, null, normals, indices, null, null);
    return createVao(meshData);
  }

  public Matrix4f getTransformationMatrix() {
    return Maths.createTransformationMatrix(position, new Quaternion(rotation), scale);
  }

  public Vector3f getPosition() {
    return position;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public Vao getModel() {
    return model;
  }

  public Vector4f getColour() {
    return colour;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public void setRotation(Vector3f rotation) {
    this.rotation = rotation;
  }

  private static Vao createVao(MeshData data) {
    Vao vao = Vao.create();
    vao.bind();
    vao.createIndexBuffer(data.getIndices());
    vao.createAttribute(0, data.getVertices(), 3);
    vao.createAttribute(2, data.getNormals(), 3);
    vao.unbind();
    return vao;
  }
}
