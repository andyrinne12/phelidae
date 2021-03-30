#version 150

// const vec2 lightBias = vec2(0.7, 0.6);//just indicates the balance between diffuse and ambient lighting

in vec2 pass_textureCoords;
flat in vec3 pass_normal;

out vec4 out_colour;

uniform sampler2D diffuseMap;
uniform vec3 lightDirection;
uniform vec4 shapeColour;

void main(void){
	
	vec4 diffuseColour = shapeColour;
	vec3 unitNormal = normalize(pass_normal);
	float diffuseLight = max(dot(lightDirection, unitNormal) * 0.003f, 0.85f);
	// * lightBias.x+ lightBias.y;
	out_colour = diffuseColour * diffuseLight;
	
}