#version 330




in vec2 outTexCoord;
in vec3 mvVertexPos;
in vec3 mvVertexNormal;


uniform struct PointLight
{
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec3 attenuation;
} pointLight;

uniform struct Material
{
	sampler2D texture;

	vec4  ambient;
	vec4  diffuse;
	vec4  specular;
	vec4  emission;
	float shininess;
} material;

in Vertex {
	vec2  texcoord;
	vec3  normal;
	vec3  lightDir;
	vec3  viewDir;
	float distance;
} Vert;

//uniform vec3 camera_pos;
//uniform sampler2D texture_sampler;



  void main()
  {
   // vec4 baseColour = texture(texture_sampler, outTexCoord);
   // gl_FragColor = baseColour* ( vec4(0.1,0.1,0.1,1) );


	// нормализуем полученные данные дл€ коррекции интерпол€ции
	vec3 normal   = normalize(Vert.normal);
	vec3 lightDir = normalize(Vert.lightDir);
	vec3 viewDir  = normalize(Vert.viewDir);

	// коэффициент затухани€
	float attenuation = 1.0 / (pointLight.attenuation[0] +
		pointLight.attenuation[1] * Vert.distance +
		pointLight.attenuation[2] * Vert.distance * Vert.distance);

	// добавим собственное свечение материала
    	//gl_FragColor = material.emission;
    gl_FragColor = material.emission;
	// добавим фоновое освещение
    	//gl_FragColor += material.ambient * pointLight.ambient * attenuation;
	gl_FragColor += material.ambient * pointLight.ambient * attenuation;

	// добавим рассе€нный свет
	float NdotL = max(dot(normal, lightDir), 0.05);
	gl_FragColor += material.diffuse * pointLight.diffuse * NdotL  * attenuation;

	// добавим отраженный свет
	float RdotVpow = max(pow(dot(reflect(-lightDir, normal), viewDir), material.shininess), 0.05);
	gl_FragColor += material.specular * pointLight.specular *RdotVpow* attenuation;

	// вычислим итоговый цвет пиксел€ на экране с учетом текстуры
	gl_FragColor *= texture(material.texture, Vert.texcoord);



  }