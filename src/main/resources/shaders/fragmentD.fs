#version 330


uniform sampler2DShadow depthTexture;
uniform sampler2D textureMap;

uniform struct DirectionalLight
{
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
} directionalLight;


in Vertex {
	vec2  texcoord;
	vec3  normal;
	vec3  lightDir;
	vec3  viewDir;
	vec3  color;
	vec4 smcoord;
} Vert;

float PCF(in vec4 smcoord)
{
	float res = 0.0;

	res += textureProjOffset(depthTexture, smcoord, ivec2(-1,-1),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 0,-1),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 1,-1),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2(-1, 0),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 0, 0),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 1, 0),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2(-1, 1),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 0, 1),1);
	res += textureProjOffset(depthTexture, smcoord, ivec2( 1, 1),1);

	return (res / 9.0);
}


  void main()
  {

     vec4 ambient = vec4(0.5f, 0.5f, 0.5f, 1.0f);
     vec4 diffuse = vec4 (0.8f, 0.8f, 0.8f, 1.0f);
     vec4 specular = vec4(0.1f, 0.1f, 0.1f, 1.0f);
     vec4 emission = vec4(0.0f, 0.0f, 0.0f, 1.0f);
     float shininess = 20.0f;


  	vec4 texture = texture(textureMap, Vert.texcoord);

	if(texture.a<0.1){
		discard;
	}

	// ??????????? ?????????? ?????? ??? ????????? ????????????
	vec3 normal   = normalize(Vert.normal);
	vec3 lightDir = normalize(Vert.lightDir);
	vec3 viewDir  = normalize(Vert.viewDir);

	//vec4 smcoord = Vert.smcoord;
	//smcoord.z-=0.005;

	float shadow  = textureProj(depthTexture, Vert.smcoord); //PCF(smcoord);

	// ??????? ??????????? ???????? ?????????

    gl_FragColor = emission;
	// ??????? ??????? ?????????

	gl_FragColor += ambient * directionalLight.ambient ;


    vec4 light;
	// ??????? ?????????? ????
	float NdotL = max(dot(normal, lightDir), 0.0);
	light += diffuse * directionalLight.diffuse * NdotL  ;

	// ??????? ?????????? ????
	float RdotVpow = max(pow(dot(reflect(-lightDir, normal), viewDir), shininess), 0.05);
	light += specular * directionalLight.specular *RdotVpow;

    gl_FragColor+=light * shadow;
	gl_FragColor *= vec4(Vert.color,1);


	gl_FragColor *= texture  ;



  }

