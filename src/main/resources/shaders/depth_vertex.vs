#version 330

layout (location=0) in vec3 position;

uniform mat4 modelViewProjection;



void main(void)
{
	// переводим координаты вершины в однородные
	gl_Position = modelViewProjection * vec4(position, 1.0);
}
