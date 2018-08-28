#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 inColour;
layout (location=2) in float opacity;

out vec3 exColour;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    exColour = inColour;
}