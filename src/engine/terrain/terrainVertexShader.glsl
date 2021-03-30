#version 400 core

in vec3 in_position;
in vec2 in_textureCoords;
in vec3 in_normal;

out vec2 pass_textureCoords;
flat out vec3 pass_surfaceNormal;
out vec3 pass_toLightVector;
out vec3 pass_toCameraVector;

uniform mat4 projectionViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightDirection;

void main(void){

    gl_Position = projectionViewMatrix * transformationMatrix * vec4(in_position,1.0);
    pass_textureCoords = in_textureCoords;

    pass_surfaceNormal = in_normal;
    pass_toLightVector = lightDirection;
    pass_toCameraVector = (inverse(projectionViewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz;

}