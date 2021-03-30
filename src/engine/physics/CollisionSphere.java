package engine.physics;

import actors.AnimatedEntity;
import animation.animation.Quaternion;
import engine.scene.Scene;
import engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shapes.Shape;

public class CollisionSphere {

  private final float radius;
  private final Shape marker;
  private final AnimatedEntity entity;
  private Vector3f position;
  private final Vector3f offset;

  public CollisionSphere(float radius, AnimatedEntity entity, Vector3f offset) {
    this.radius = radius;
    this.marker = new Shape(radius);
    this.entity = entity;
    this.offset = offset;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void update(Matrix4f transform) {
    Vector4f tempVec = new Vector4f(offset.x, offset.y, offset.z, 1);
    Matrix4f modelTransform = Maths
        .createTransformationMatrix(new Vector3f(0, 0, 0), entity.getFinalRotation(),
            entity.getScale());

    Matrix4f.transform(transform, tempVec, tempVec);
    Matrix4f.transform(modelTransform, tempVec, tempVec);

    Vector3f transOffset = new Vector3f(tempVec.x, tempVec.y, tempVec.z);

    position = Vector3f.add(entity.getPosition(), transOffset, null);
    marker.setPosition(position);
 //   marker.setRotation(entity.getFinalRotation().toEuler());
  }

  public void update() {
    this.position = Vector3f.add(entity.getPosition(), offset, null);
    marker.setPosition(position);
  }

  public void showMarker(Scene scene) {
    scene.addShape(marker);
  }

  public void hideMarker(Scene scene) {
    scene.removeShape(marker);
  }


}
