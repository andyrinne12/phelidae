package engine.terrain;

import animation.renderer.AnimatedModelRenderer;
import engine.shaders.ShaderProgram;
import engine.shaders.UniformFloat;
import engine.shaders.UniformMatrix;
import engine.shaders.UniformSampler;
import engine.shaders.UniformVec3;
import engine.utils.MyFile;

public class TerrainShader extends ShaderProgram {

  private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
  private static final int DIFFUSE_TEX_UNIT = 0;

  private static final MyFile VERTEX_SHADER = new MyFile("src/engine/terrain",
      "terrainVertexShader.glsl");
  private static final MyFile FRAGMENT_SHADER = new MyFile("src/engine/terrain",
      "terrainFragmentShader.glsl");

  protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
  protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
  protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
  protected UniformFloat shineDamper = new UniformFloat("shineDamper");
  protected UniformFloat reflectivity = new UniformFloat("reflectivity");
  protected UniformFloat backgroundTexture = new UniformFloat("backgroundTexture");
  protected UniformSampler rTexture = new UniformSampler("rTexture");
  protected UniformSampler gTexture = new UniformSampler("gTexture");
  protected UniformSampler bTexture = new UniformSampler("bTexture");
  protected UniformSampler blendMap = new UniformSampler("blendMap");

  private UniformSampler diffuseMap = new UniformSampler("diffuseMap");

  /**
   * Creates the shader program for the {@link AnimatedModelRenderer} by loading up the vertex and
   * fragment shader code files. It also gets the location of all the specified uniform variables,
   * and also indicates that the diffuse texture will be sampled from texture unit 0.
   */
  public TerrainShader() {
    super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords", "in_normal");
    super.storeAllUniformLocations(projectionViewMatrix, transformationMatrix, lightDirection);
    connectTextureUnits();
  }

  /**
   * Indicates which texture unit the diffuse texture should be sampled from.
   */
  private void connectTextureUnits() {
    super.start();
    //    diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
    super.stop();
  }

}