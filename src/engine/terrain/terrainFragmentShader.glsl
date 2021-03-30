#version 400 core

in vec2 pass_textureCoords;
flat in vec3 pass_surfaceNormal;
in vec3 pass_toLightVector;
in vec3 pass_toCameraVector;

out vec4 out_Color;


uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour;

uniform float shineDamper;
uniform float reflectivity;


void main(void){

    vec4 blue = vec4(0.5, 0.5, 1.0, 1.0);

    /*
        vec4 blendMapColour = texture(blendMap,pass_textureCoords);

        float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
        vec2 tiledCoords = pass_textureCoords * 40;
        vec4 backgroundTextureColour = texture(backgroundTexture,tiledCoords) * backTextureAmount;
        vec4 rTextureColour = texture(rTexture,tiledCoords) * blendMapColour.r;
        vec4 gTextureColour = texture(gTexture,tiledCoords) * blendMapColour.g;
        vec4 bTextureColour = texture(bTexture,tiledCoords) * blendMapColour.b;

        vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;
    */

    // diffuse light

    vec3 unitNormal = normalize(pass_surfaceNormal);
    vec3 unitLightVector = normalize(pass_toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.4f);
    vec3 lightColour = vec3(1.0, 1.0, 1.0);
    vec3 diffuse = brightness * lightColour;

    // specular light

    vec3 unitVectorToCamera = normalize(pass_toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    //   out_Color = vec4(diffuse,1.0) * (1.0,0.0,0.0,1.0) + vec4(finalSpecular,1.0);
    out_Color = vec4(diffuse, 1.0) * vec4(1.0, 0.3, 0.5, 1.0);
}