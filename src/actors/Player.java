package actors;

import animation.animatedModel.AnimatedModel;
import animation.animation.Animator;
import engine.scene.Scene;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends AnimatedEntity {

  private final float RUN_ACCEL = 10f;
  private final float TURN_SPEED = (float) Math.PI/6;

  public Player(AnimatedModel model, Vector3f position, Vector3f rotation, float scale,
      Scene scene) {
    super(model, position, rotation, scale, scene);
  }

  @Override
  public void update(float delta, Scene scene) {
    checkInputs();
    if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
      scene.getCamera().focusPlayer(scene);
    }
    super.update(delta, scene);
  }

  private void checkInputs() {

    if (isInAir) {
      turnSpeed = 0;
      return;
    }

    Animator animator = getModel().getAnimator();

    if (!isInAir) {

      if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
        horizontalAccel = RUN_ACCEL;

//        if (animator.getCurrentAnimation() == null) {
//          animator.doAnimation(null);
//        }

      } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
        horizontalAccel = -RUN_ACCEL;
      } else {
        horizontalAccel = 0;
      }

      if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        turnSpeed = TURN_SPEED;
      } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
        turnSpeed = -TURN_SPEED;
      } else {
        turnSpeed = 0;
      }

      if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        super.jump();
      }
    }

  }
}
