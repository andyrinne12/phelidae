#version 150

// const vec2 lightBias = vec2(0.7, 0.6);//just indicates the balance between diffuse and ambient lighting

in vec2 pass_textureCoords;
flat in vec3 normal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_colour;

uniform sampler2D diffuseMap;
uniform vec3 lightColours[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 lightDirection;

void main(void){
	
	vec4 diffuseColour = vec4(0,0.5,0.5,1.0);
	vec3 unitNormal = normalize(normal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for(int i = 0; i < 4;i++){

		vec3 unitLightVector = normalize(toLightVector[i]);

		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);

		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);

		//float diffuseLight = max(dot(lightDirection, unitNormal) * 0.003f, 0.85f);

		totalDiffuse = totalDiffuse + brightness * lightColours[i];
		totalSpecular = totalSpecular + dampedFactor * reflectivity * lightColours[i];
	}

	totalDiffuse = max(totalDiffuse, 0.2f);

	// * lightBias.x+ lightBias.y;
	out_colour = vec4(totalDiffuse, 1.0) * diffuseColour ;
	
}