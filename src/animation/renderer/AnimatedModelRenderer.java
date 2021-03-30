package animation.renderer;

import actors.AnimatedEntity;
import animation.animatedModel.AnimatedModel;
import engine.scene.ICamera;
import engine.scene.Light;
import engine.utils.OpenGlUtils;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


/**
 * This class deals with rendering an animated entity. Nothing particularly new here. The only
 * exciting part is that the joint transforms get loaded up to the shader in a uniform array.
 *
 * @author Karl
 */
public class AnimatedModelRenderer {

  private AnimatedModelShader shader;

  /**
   * Initializes the shader program used for rendering animated models.
   */
  public AnimatedModelRenderer() {
    this.shader = new AnimatedModelShader();
  }

  /**
   * Renders an animated entity. The main thing to note here is that all the joint transforms are
   * loaded up to the shader to a uniform array. Also 5 attributes of the VAO are enabled before
   * rendering, to include joint indices and weights.
   *
   * @param entity   - the animated entity to be rendered.
   * @param camera   - the camera used to render the entity.
   * @param lightDir - the direction of the light in the scene.
   */
  public void render(AnimatedEntity entity, ICamera camera, List<Light> lights, Vector3f lightDir) {
    prepare(camera, lights, lightDir);
    AnimatedModel model = entity.getModel();
    model.getTexture().bindToUnit(0);
    model.getModel().bind(0, 1, 2, 3, 4);
    shader.transformationMatrix.loadMatrix(entity.getTransformationMatrix());
    shader.jointTransforms.loadMatrixArray(model.getJointTransforms());
    GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT,
        0);
    model.getModel().unbind(0, 1, 2, 3, 4);
    finish();
  }

  /**
   * Deletes the shader program when the game closes.
   */
  public void cleanUp() {
    shader.cleanUp();
  }

  /**
   * Starts the shader program and loads up the projection view matrix, as well as the light
   * direction. Enables and disables a few settings which should be pretty self-explanatory.
   *
   * @param camera   - the camera being used.
   * @param lightDir - the direction of the light in the scene.
   */
  private void prepare(ICamera camera, List<Light> lights, Vector3f lightDir) {
    shader.start();
    shader.projectionMatrix.loadMatrix(camera.getProjectionMatrix());
    shader.viewMatrix.loadMatrix(camera.getViewMatrix());
    shader.reflectivity.loadFloat(0.5f);
    shader.shineDamper.loadFloat(0.3f);
    loadLights(lights);
    OpenGlUtils.antialias(true);
    OpenGlUtils.disableBlending();
    OpenGlUtils.enableDepthTesting(true);
  }

  private void loadLights(List<Light> lights) {
    Vector3f[] lightPositions = new Vector3f[AnimatedModelShader.MAX_LIGHTS];
    Vector3f[] lightColours = new Vector3f[AnimatedModelShader.MAX_LIGHTS];
    for (int i = 0; i < AnimatedModelShader.MAX_LIGHTS; i++) {
      if (i < lights.size()) {
        lightPositions[i] = lights.get(i).getPosition();
        lightColours[i] = lights.get(i).getColour();
      } else {
        lightPositions[i] = new Vector3f(0, 0, 0);
        lightColours[i] = new Vector3f(0, 0, 0);
      }
    }
    shader.lightPositions.loadVec3Array(lightPositions);
    shader.lightColours.loadVec3Array(lightColours);
  }

  /**
   * Stops the shader program after rendering the entity.
   */
  private void finish() {
    shader.stop();
  }

}
