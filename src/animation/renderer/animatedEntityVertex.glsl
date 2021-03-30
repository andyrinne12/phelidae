#version 150

const int MAX_JOINTS = 100;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 5;//max number of joints that can affect a vertex

in vec3 in_position;
in vec2 in_textureCoords;
in vec3 in_normal;

in ivec3 in_jointIndices;
in vec3 in_weights;

out vec2 pass_textureCoords;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;
flat out vec3 normal;

uniform mat4 jointTransforms[MAX_JOINTS];
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightPositions[4];

uniform float useFakeLighting;



void main(void){

    vec4 totalLocalPos = vec4(0.0);
    vec4 totalNormal = vec4(0.0);

    for (int i=0;i<MAX_WEIGHTS;i++){
        mat4 jointTransform = jointTransforms[in_jointIndices[i]];
        vec4 posePosition = jointTransform * vec4(in_position, 1.0);
        totalLocalPos += posePosition * in_weights[i];

        vec4 worldNormal = jointTransform * vec4(in_normal, 0.0);
        totalNormal += worldNormal * in_weights[i];
    }

    vec4 worldPosition = transformationMatrix * totalLocalPos;
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;

    normal = (transformationMatrix * totalNormal).xyz;

    for (int i = 0; i < 4; i++){
        toLightVector[i] = lightPositions[i] - worldPosition.xyz;
    }

    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length((positionRelativeToCam).xyz);

    pass_textureCoords = in_textureCoords;

}