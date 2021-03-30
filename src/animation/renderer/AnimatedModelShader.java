package animation.renderer;


import engine.shaders.ShaderProgram;
import engine.shaders.UniformFloat;
import engine.shaders.UniformMat4Array;
import engine.shaders.UniformMatrix;
import engine.shaders.UniformSampler;
import engine.shaders.UniformVec3Array;
import engine.utils.MyFile;

public class AnimatedModelShader extends ShaderProgram {

  private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
  private static final int DIFFUSE_TEX_UNIT = 0;
  protected static final int MAX_LIGHTS = 4;

  private static final MyFile VERTEX_SHADER = new MyFile("src/animation/renderer",
      "animatedEntityVertex.glsl");
  private static final MyFile FRAGMENT_SHADER = new MyFile("src/animation/renderer",
      "animatedEntityFragment.glsl");

  protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
  protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
  protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
  protected UniformVec3Array lightPositions = new UniformVec3Array("lightPositions", MAX_LIGHTS);
  protected UniformVec3Array lightColours = new UniformVec3Array("lightColours", MAX_LIGHTS);
  protected UniformFloat shineDamper = new UniformFloat("shineDamper");
  protected UniformFloat reflectivity = new UniformFloat("reflectivity");

  protected UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", MAX_JOINTS);
 // private UniformSampler diffuseMap = new UniformSampler("diffuseMap");

  /**
   * Creates the shader program for the {@link AnimatedModelRenderer} by loading up the vertex and
   * fragment shader code files. It also gets the location of all the specified uniform variables,
   * and also indicates that the diffuse texture will be sampled from texture unit 0.
   */
  public AnimatedModelShader() {
    super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords", "in_normal",
        "in_jointIndices",
        "in_weights");
    super.storeAllUniformLocations(projectionMatrix, viewMatrix, transformationMatrix,
        lightPositions, lightColours, shineDamper, reflectivity,
        jointTransforms);
    connectTextureUnits();
  }

  /**
   * Indicates which texture unit the diffuse texture should be sampled from.
   */
  private void connectTextureUnits() {
    super.start();
  //  diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
    super.stop();
  }

}
