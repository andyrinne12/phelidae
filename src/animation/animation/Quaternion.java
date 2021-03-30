package animation.animation;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * A quaternion simply represents a 3D rotation. The maths behind it is quite complex (literally; it
 * involves complex numbers) so I wont go into it in too much detail. The important things to note
 * are that it represents a 3d rotation, it's very easy to interpolate between two quaternion
 * rotations (which would not be easy to do correctly with Euler rotations or rotation matrices),
 * and you can convert to and from matrices fairly easily. So when we need to interpolate between
 * rotations we'll represent them as quaternions, but when we need to apply the rotations to
 * anything we'll convert back to a matrix.
 * <p>
 * An quick introduction video to quaternions: https://www.youtube.com/watch?v=SCbpxiCN0U0
 * <p>
 * and a slightly longer one: https://www.youtube.com/watch?v=fKIss4EV6ME&t=0s
 *
 * @author Karl
 */
public class Quaternion {

  private float x, y, z, w;

  /**
   * Creates a quaternion and normalizes it.
   *
   * @param x
   * @param y
   * @param z
   * @param w
   */
  public Quaternion(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
    normalize();
  }

  public Quaternion(Vector3f axis, float rotation) {
    axis.normalise(axis);
    float sin = (float) Math.sin(rotation / 2);
    this.x = axis.x * sin;
    this.y = axis.y * sin;
    this.z = axis.z * sin;
    this.w = (float) Math.cos(rotation / 2);
    normalize();
  }

  public Quaternion(Vector3f rotation) {
    float bank = rotation.x;
    float heading = rotation.y;
    float attitude = rotation.z;
    double c1 = Math.cos(heading / 2);
    double s1 = Math.sin(heading / 2);
    double c2 = Math.cos(attitude / 2);
    double s2 = Math.sin(attitude / 2);
    double c3 = Math.cos(bank / 2);
    double s3 = Math.sin(bank / 2);
    double c1c2 = c1 * c2;
    double s1s2 = s1 * s2;
    w = (float) (c1c2 * c3 - s1s2 * s3);
    x = (float) (c1c2 * s3 + s1s2 * c3);
    y = (float) (s1 * c2 * c3 + c1 * s2 * s3);
    z = (float) (c1 * s2 * c3 - s1 * c2 * s3);
    normalize();
  }

  /**
   * Extracts the rotation part of a transformation matrix and converts it to a quaternion using the
   * magic of maths.
   * <p>
   * More detailed explanation here: http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
   *
   * @param matrix - the transformation matrix containing the rotation which this quaternion shall
   *               represent.
   */
  public static Quaternion fromMatrix(Matrix4f matrix) {
    float w, x, y, z;
    float diagonal = matrix.m00 + matrix.m11 + matrix.m22;
    if (diagonal > 0) {
      float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
      w = w4 / 4f;
      x = (matrix.m21 - matrix.m12) / w4;
      y = (matrix.m02 - matrix.m20) / w4;
      z = (matrix.m10 - matrix.m01) / w4;
    } else if ((matrix.m00 > matrix.m11) && (matrix.m00 > matrix.m22)) {
      float x4 = (float) (Math.sqrt(1f + matrix.m00 - matrix.m11 - matrix.m22) * 2f);
      w = (matrix.m21 - matrix.m12) / x4;
      x = x4 / 4f;
      y = (matrix.m01 + matrix.m10) / x4;
      z = (matrix.m02 + matrix.m20) / x4;
    } else if (matrix.m11 > matrix.m22) {
      float y4 = (float) (Math.sqrt(1f + matrix.m11 - matrix.m00 - matrix.m22) * 2f);
      w = (matrix.m02 - matrix.m20) / y4;
      x = (matrix.m01 + matrix.m10) / y4;
      y = y4 / 4f;
      z = (matrix.m12 + matrix.m21) / y4;
    } else {
      float z4 = (float) (Math.sqrt(1f + matrix.m22 - matrix.m00 - matrix.m11) * 2f);
      w = (matrix.m10 - matrix.m01) / z4;
      x = (matrix.m02 + matrix.m20) / z4;
      y = (matrix.m12 + matrix.m21) / z4;
      z = z4 / 4f;
    }
    return new Quaternion(x, y, z, w);
  }

  /**
   * Interpolates between two quaternion rotations and returns the resulting quaternion rotation.
   * The interpolation method here is "nlerp", or "normalized-lerp". Another mnethod that could be
   * used is "slerp", and you can see a comparison of the methods here:
   * https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
   * <p>
   * and here: http://number-none.com/product/Understanding%20Slerp,%20Then%20Not%20Using%20It/
   *
   * @param a
   * @param b
   * @param blend - a value between 0 and 1 indicating how far to interpolate between the two
   *              quaternions.
   * @return The resulting interpolated rotation in quaternion format.
   */
  public static Quaternion interpolate(Quaternion a, Quaternion b, float blend) {
    Quaternion result = new Quaternion(0, 0, 0, 1);
    float dot = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
    float blendI = 1f - blend;
    if (dot < 0) {
      result.w = blendI * a.w + blend * -b.w;
      result.x = blendI * a.x + blend * -b.x;
      result.y = blendI * a.y + blend * -b.y;
      result.z = blendI * a.z + blend * -b.z;
    } else {
      result.w = blendI * a.w + blend * b.w;
      result.x = blendI * a.x + blend * b.x;
      result.y = blendI * a.y + blend * b.y;
      result.z = blendI * a.z + blend * b.z;
    }
    result.normalize();
    return result;
  }

  /*
   * Multiply two Quaternions
   * @param left Left term
   * @param right Right term
   * @return product
   */
  public static Quaternion mul(Quaternion left, Quaternion right) {

    float rot = left.w * right.w - Vector3f.dot(left.getAxis(), right.getAxis());
    Vector3f rightTerm = (Vector3f) right.getAxis().scale(left.w);
    Vector3f leftTerm = (Vector3f) left.getAxis().scale(right.w);
    Vector3f cross = Vector3f.cross(left.getAxis(), right.getAxis(), null);
    Vector3f sum = Vector3f.add(cross, Vector3f.add(leftTerm, rightTerm, null), null);
    return new Quaternion(sum, rot);
  }

  public static Quaternion multiply(Quaternion q1, Quaternion q2) {
    float w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
    float x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y;
    float y = q1.w * q2.y - q1.x * q2.z + q1.y * q2.w + q1.z * q2.x;
    float z = q1.w * q2.z + q1.x * q2.y - q1.y * q2.x + q1.z * q2.w;
    return new Quaternion(x, y, z, w);
  }

  /**
   * Normalizes the quaternion.
   */
  public void normalize() {
    float mag = (float) Math.sqrt(w * w + x * x + y * y + z * z);
    w /= mag;
    x /= mag;
    y /= mag;
    z /= mag;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("Quat(").append(x).append(',').append(y).append(',').append(z).append(',').append(w)
        .append(')');
    return str.toString();
  }

  /**
   * Converts the quaternion to a 4x4 matrix representing the exact same rotation as this
   * quaternion. (The rotation is only contained in the top-left 3x3 part, but a 4x4 matrix is
   * returned here for convenience seeing as it will be multiplied with other 4x4 matrices).
   * <p>
   * More detailed explanation here: http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/
   *
   * @return The rotation matrix which represents the exact same rotation as this quaternion.
   */

  public Vector3f toEuler() {
    float angX = (float) Math.atan2(2 * (w * x + y * z), 1 - 2 * (x * x + y * y));
    float angY = (float) Math.asin(2 * (w * y - x * z));
    float angZ = (float) Math.atan2(2 * (w * z + x * y), 1 - 2 * (y * y + z * z));
    return new Vector3f(angX, angY, angZ);
  }

  public Matrix4f toRotationMatrix() {
    Matrix4f matrix = new Matrix4f();
    final float xy = x * y;
    final float xz = x * z;
    final float xw = x * w;
    final float yz = y * z;
    final float yw = y * w;
    final float zw = z * w;
    final float xSquared = x * x;
    final float ySquared = y * y;
    final float zSquared = z * z;
    matrix.m00 = 1 - 2 * (ySquared + zSquared);
    matrix.m01 = 2 * (xy - zw);
    matrix.m02 = 2 * (xz + yw);
    matrix.m03 = 0;
    matrix.m10 = 2 * (xy + zw);
    matrix.m11 = 1 - 2 * (xSquared + zSquared);
    matrix.m12 = 2 * (yz - xw);
    matrix.m13 = 0;
    matrix.m20 = 2 * (xz - yw);
    matrix.m21 = 2 * (yz + xw);
    matrix.m22 = 1 - 2 * (xSquared + ySquared);
    matrix.m23 = 0;
    matrix.m30 = 0;
    matrix.m31 = 0;
    matrix.m32 = 0;
    matrix.m33 = 1;
    return matrix;
  }

  public float getRotation() {
    return w;
  }

  public Vector3f getAxis() {
    return new Vector3f(x, y, z);
  }

}
