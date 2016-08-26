#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 colors;
layout (location=2) in vec2 textures;
layout (location=3) in vec3 vertexNormal;




uniform vec3 viewPosition;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 normal;
uniform mat4 lightMatrix;
uniform mat4 bias;

out Vertex {
	vec2  texcoord;
	vec3  normal;
	vec3  lightDir;
	vec3  viewDir;
	vec3  color;
	vec4 smcoord;
} Vert;

uniform struct DirectionalLight
{
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;

} directionalLight;


void main()
{


	Vert.color = colors;
	// ��������� ���������� ������� � ������� ������� ���������
	vec4 vertex   = modelViewMatrix * vec4(position, 1.0);


	Vert.smcoord  =  lightMatrix   * vertex ;
	// ��������� � ����������� ������ ��������� ���������
	// �������� ���������� ����������
	Vert.texcoord = textures;
	// �������� ������� � ������� ������� ���������
	Vert.normal   = normal * vertexNormal;
	// �������� ����������� �� �������� ���������
	Vert.lightDir = vec3(directionalLight.position);
	// �������� ����������� �� ������� � ����������� � ������� ������� ���������
	Vert.viewDir  = viewPosition - vec3(vertex);


	// ��������� ���������� ������� � ����������
	gl_Position = projectionMatrix * vertex;




}