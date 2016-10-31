#version 330

uniform sampler2D textureMap;

in Vertex {
	vec2  texcoord;
	vec3  color;
} Vert;




  void main()
  {

  	vec4 texture = texture(textureMap, Vert.texcoord);

	if(texture.a<0.1){
		discard;
	}

    gl_FragColor += vec4(1,1,1,1) ;
	gl_FragColor *= vec4(Vert.color,1);
	gl_FragColor *= texture ;
  }

