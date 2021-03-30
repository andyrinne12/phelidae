package engine.shaders;

import org.lwjgl.util.vector.Vector3f;

public class UniformVec3Array extends Uniform {

  private final UniformVec3[] vec3Uniforms;

  public UniformVec3Array(String name, int size) {
    super(name);
    vec3Uniforms = new UniformVec3[size];
    for (int i = 0; i < size; i++) {
      vec3Uniforms[i] = new UniformVec3(name + "[" + i + "]");
    }
  }

  @Override
  protected void storeUniformLocation(int programID) {
    for (UniformVec3 vec3Uniform : vec3Uniforms) {
      vec3Uniform.storeUniformLocation(programID);
    }
  }

  public void loadVec3Array(Vector3f[] vec3s) {
    for (int i = 0; i < vec3s.length; i++) {
      vec3Uniforms[i].loadVec3(vec3s[i]);
    }
  }


}
