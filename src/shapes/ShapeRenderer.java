package shapes;

import engine.openglObjects.Vao;
import engine.scene.ICamera;
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
public class ShapeRenderer {

  private ShapeShader shader;

  /**
   * Initializes the shader program used for rendering animated models.
   */
  public ShapeRenderer() {
    this.shader = new ShapeShader();
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

  public void render(List<Shape> shapes, ICamera camera, Vector3f lightDir) {
    prepare(camera, lightDir);
    for (Shape shape : shapes) {
      Vao model = shape.getModel();
      model.bind(0, 2);
      shader.transformationMatrix.loadMatrix(shape.getTransformationMatrix());
      GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
      model.unbind(0, 2);
    }
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
  private void prepare(ICamera camera, Vector3f lightDir) {
    shader.start();
    shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
    shader.lightDirection.loadVec3(lightDir);
    shader.shapeColour.loadVec4(Shape.YELLOW);
    OpenGlUtils.antialias(true);
    OpenGlUtils.disableBlending();
    OpenGlUtils.enableDepthTesting(true);
    OpenGlUtils.cullBackFaces(false);
  }

  /**
   * Stops the shader program after rendering the entity.
   */
  private void finish() {
    shader.stop();
  }

}
