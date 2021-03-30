package actors;

import animation.animatedModel.AnimatedModel;
import animation.animation.Quaternion;
import engine.physics.CollisionSphere;
import engine.physics.TerrainContact;
import engine.scene.Scene;
import engine.terrain.Terrain;
import engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class AnimatedEntity {

  private static final float PI = (float) Math.PI;
  protected final Vector3f position;
  protected final Vector3f directionRotation;
  // protected final float GROUND_LEVEL = 1f;
  protected final float GRAVITY = 25;
  protected final float JUMP_POWER = 7;
  protected final float MODEL_ROT_Y_CORRECTION = (float) Math.toRadians(90);
  protected final float STOP_ACCEL = 25f;
  protected final float MAX_SPEED = 5f;
  protected final float CENTER_HEIGHT = 0.43f;
  protected final float baseAnimationSpeed = 3.3f;
  protected final float maxAnimationSpeed = 5f;
  private final AnimatedModel model;
  protected Quaternion finalRotation;
  protected float scale;
  protected float IN_AIR_BIAS = 0.65f;
  protected float currAnimationSpeed;

  protected float slopeAngle = 0;
  protected float verticalSpeed = 0;
  protected float horizontalSpeed = 0;
  protected float horizontalAccel = 0;
  protected float turnSpeed = 0;
  protected boolean isInAir = false;

  protected Vector3f FRONT_FEET_OFFSET = new Vector3f(0, -0.8f, 1.1f);
  protected Vector3f BACK_FEET_OFFSET = new Vector3f(0, -0.8f, -0.52f);

  protected Vector3f FRONT_LEFT_FOOT_OFS = new Vector3f(0.1f, -0.72f, 0.59f);
  protected String FRONT_LEFT_FOOT_JOINT = "frontleg_foot_L";

  protected Vector3f FRONT_RIGHT_FOOT_OFS = new Vector3f(-0.1f, -0.72f, 0.59f);
  protected String FRONT_RIGHT_FOOT_JOINT = "frontleg_foot_R";

  protected Vector3f BACK_LEFT_FOOT_OFS = new Vector3f(0.1f, -0.72f, -0.39f);
  protected String BACK_LEFT_FOOT_JOINT = "backleg_foot_L";

  protected Vector3f BACK_RIGHT_FOOT_OFS = new Vector3f(-0.1f, -0.72f, -0.39f);
  protected String BACK_RIGHT_FOOT_JOINT = "backleg_foot_R";

  protected TerrainContact frontFeetContact;
  protected TerrainContact backFeetContact;

  protected CollisionSphere frontLeftFootColl;
  protected CollisionSphere frontRightFootColl;
  protected CollisionSphere backLeftFootColl;
  protected CollisionSphere backRightFootColl;

  //protected Shape directionMarker = new Shape(0.07f);

  public AnimatedEntity(AnimatedModel model) {
    this.model = model;
    directionRotation = new Vector3f(0, 0, 0);
    position = new Vector3f(0, 0, 0);
    scale = 1;
  }

  public AnimatedEntity(AnimatedModel model, Vector3f position, Vector3f rotation, float scale,
      Scene scene) {
    this.model = model;
    this.position = position;
    this.directionRotation = rotation;
    this.scale = scale;
    frontFeetContact = new TerrainContact(FRONT_FEET_OFFSET);
    backFeetContact = new TerrainContact(BACK_FEET_OFFSET);
//    frontFeetContact.showMarker(scene);
//    backFeetContact.showMarker(scene);

    frontLeftFootColl = new CollisionSphere(0.03f, this, FRONT_LEFT_FOOT_OFS);
    frontLeftFootColl.showMarker(scene);

    frontRightFootColl = new CollisionSphere(0.03f, this, FRONT_RIGHT_FOOT_OFS);
    frontRightFootColl.showMarker(scene);

    backLeftFootColl = new CollisionSphere(0.03f, this, BACK_LEFT_FOOT_OFS);
    backLeftFootColl.showMarker(scene);

    backRightFootColl = new CollisionSphere(0.03f, this, BACK_RIGHT_FOOT_OFS);
    backRightFootColl.showMarker(scene);
  }

  public void update(float delta, Scene scene) {
    handleJump(delta, scene);
    handleMovement(delta);
    if (horizontalSpeed == 0 || isInAir) {
      model.update(0);
    } else {
      model.update(currAnimationSpeed * Math.signum(horizontalSpeed));
    }

    frontFeetContact.update(this);
    backFeetContact.update(this);

    frontLeftFootColl.update(model.getJointTransformation(FRONT_LEFT_FOOT_JOINT));
    frontRightFootColl.update(model.getJointTransformation(FRONT_RIGHT_FOOT_JOINT));
    backLeftFootColl.update(model.getJointTransformation(BACK_LEFT_FOOT_JOINT));
    backRightFootColl.update(model.getJointTransformation(BACK_RIGHT_FOOT_JOINT));
  }

  public void jump() {
    if (!isInAir) {
      isInAir = true;
      verticalSpeed = JUMP_POWER;
    }
  }

  public void move(float dx, float dy, float dz) {
    position.x += dx;
    position.y += dy;
    position.z += dz;
  }

  public void directionRotate(float dx, float dy, float dz) {
    directionRotation.x = (directionRotation.x + dx) % (2 * PI);
    directionRotation.y = (directionRotation.y + dy) % (2 * PI);
    directionRotation.z = (directionRotation.z + dz) % (2 * PI);
  }

  public Vector3f getPosition() {
    return position;
  }

  public Vector3f getRotation() {
    return directionRotation;
  }

  public Quaternion getFinalRotation() {
    return finalRotation;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public Matrix4f getTransformationMatrix() {
    return Maths.createTransformationMatrix(position, finalRotation, scale);
  }

  public AnimatedModel getModel() {
    return model;
  }

  public float getAnimationSpeed() {
    return currAnimationSpeed;
  }

  public void setAnimationSpeed(float animationSpeed) {
    this.currAnimationSpeed = animationSpeed;
  }

  //--------------------------
  //

  private void handleJump(float delta, Scene scene) {
    position.y += verticalSpeed * delta;
    verticalSpeed -= GRAVITY * delta;
    Terrain terrain = scene.getTerrains().getParcel(position.x, position.z);
    float ground_level = 0;
    if (terrain != null) {
      ground_level = terrain.getHeightOfTerrain(position.x, position.z);
    }
    float toGroundDistance = ground_level + CENTER_HEIGHT - position.y;
    if (toGroundDistance >= 0) {
      isInAir = false;
      verticalSpeed = 0;
      position.y = ground_level + CENTER_HEIGHT;
    } else if (-toGroundDistance >= IN_AIR_BIAS) {
      isInAir = true;
    }

    float frontHeight = frontFeetContact.getTerrainHeight(scene);
    float backHeight = backFeetContact.getTerrainHeight(scene);
    Vector3f frontPos = frontFeetContact.getPosition();
    Vector3f backPos = backFeetContact.getPosition();

    float distance = Maths.lineLength(frontPos.x, frontPos.z, backPos.x, backPos.z);
    distance = Math.max(distance, 0.01f);
    float slope = (frontHeight - backHeight) / distance;
    float angle = (float) Math.atan(slope);

    if (!isInAir) {
      slopeAngle = angle;
    }

    Quaternion quatRot = new Quaternion(new Vector3f(1, 0, 0), slopeAngle);
    Quaternion quatDirRot = new Quaternion(directionRotation);

    finalRotation = Quaternion.multiply(quatRot, quatDirRot);

  }

  private void handleMovement(float delta) {
    directionRotate(0, turnSpeed * delta, 0);

    if (horizontalAccel == 0) {
      if (horizontalSpeed > 0) {
        horizontalSpeed -= STOP_ACCEL * delta;
        if (horizontalSpeed < 0) {
          horizontalSpeed = 0;
        }
      } else if (horizontalSpeed < 0) {
        horizontalSpeed += STOP_ACCEL * delta;
        if (horizontalSpeed > 0) {
          horizontalSpeed = 0;
        }
      }
    } else {
      horizontalSpeed += horizontalAccel * delta;
      if (horizontalSpeed < -MAX_SPEED) {
        horizontalSpeed = -MAX_SPEED;
      } else if (horizontalSpeed > MAX_SPEED) {
        horizontalSpeed = MAX_SPEED;
      }
    }

    float distance = horizontalSpeed * delta;

    currAnimationSpeed =
        baseAnimationSpeed + (Math.abs(horizontalSpeed) / MAX_SPEED) * (maxAnimationSpeed
            - baseAnimationSpeed);
    double rot = directionRotation.y + MODEL_ROT_Y_CORRECTION;

    float dz = (float) (distance * Math.sin(rot));
    float dx = (float) (distance * Math.cos(rot));

    move(dx, 0, dz);

  }
}
