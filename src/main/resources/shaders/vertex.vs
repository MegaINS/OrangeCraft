#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 textures;
layout (location=2) in vec3 vertexNormal;
layout (location=3) in vec3 colors;


out vec2 outTexCoord;
out vec3 mvVertexPos;
out vec3 mvVertexNormal;

uniform vec3 viewPosition;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 normal;

out Vertex {
	vec2  texcoord;
	vec3  normal;
	vec3  lightDir;
	vec3  viewDir;
	float distance;
} Vert;

uniform struct PointLight
{
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec3 attenuation;
} pointLight;


void main()
{
	// ��������� ���������� ������� � ������� ������� ���������
	vec4 vertex   = modelViewMatrix * vec4(position, 1.0);

	// ����������� �� ������� �� �������� ��������� � ������� ������� ���������
	vec4 lightDir = pointLight.position  - vertex;

	// ��������� � ����������� ������ ��������� ���������
	// �������� ���������� ����������
	Vert.texcoord = textures;
	// �������� ������� � ������� ������� ���������
	Vert.normal   = normal * vertexNormal;
	// �������� ����������� �� �������� ���������
	Vert.lightDir = vec3(lightDir);
	// �������� ����������� �� ������� � ����������� � ������� ������� ���������
	Vert.viewDir  = viewPosition - vec3(vertex);
	// �������� ���������� �� ������� �� ��������� ���������
	Vert.distance = length(lightDir);

	// ��������� ���������� ������� � ����������
	gl_Position = projectionMatrix * vertex;





  //  vec4 mvPos = modelViewMatrix * vec4(position, 1.0f);
   // mvVertexPos = mvPos.xyz;
   // mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
 //   outTexCoord =textures;
 //   gl_Position = projectionMatrix* mvPos;


}