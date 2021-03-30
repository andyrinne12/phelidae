package engine.utils;

import animation.animation.Quaternion;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

  public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
    float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
    float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
    float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
    float l3 = 1.0f - l1 - l2;
    return l1 * p1.y + l2 * p2.y + l3 * p3.y;
  }

  public static Matrix4f createTransformationMatrix(Vector3f translation, Quaternion quaternion,
      float scale) {
    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();
    matrix.translate(translation);
    Matrix4f rotMatrix = quaternion.toRotationMatrix();
    Matrix4f.mul(matrix, rotMatrix, matrix);
    matrix.scale(new Vector3f(scale, scale, scale));
    return matrix;
  }

  public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation,
      float scale) {
    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();
    Matrix4f.translate(translation, matrix, matrix);
    Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), matrix, matrix);
    Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), matrix, matrix);
    Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), matrix, matrix);
    Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
    return matrix;
  }


  public static float lineLength(float x1, float z1, float x2, float z2) {
    float dx = x1 - x2;
    float dz = z1 - z2;
    return (float) Math.sqrt(dx * dx + dz * dz);
  }

}