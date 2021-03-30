#version 150

const int MAX_JOINTS = 100;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 5;//max number of joints that can affect a vertex

in vec3 in_position;
in vec2 in_textureCoords;
in vec3 in_normal;
in ivec3 in_jointIndices;
in vec3 in_weights;

out vec2 pass_textureCoords;
flat out vec3 pass_normal;

uniform mat4 jointTransforms[MAX_JOINTS];
uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;

void main(void){

	
	gl_Position = projectionViewMatrix * transformationMatrix * vec4(in_position,1.0);
	pass_normal = (transformationMatrix * vec4(in_normal,1.0)).xyz;
	pass_textureCoords = in_textureCoords;

}