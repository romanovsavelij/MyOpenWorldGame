precision mediump float;

attribute vec3 position;
attribute vec2 textureCoords;
attribute vec3 normal;

uniform mat4 transformationMatrix;
uniform vec3 lightPosition;

varying vec2 textureCoord;
varying vec3 toLightVector;
varying vec3 surfaceNormal;

void main()
{

    vec4 worldCoords = transformationMatrix * vec4(position, 1.0f);

    textureCoord = textureCoords * vec2(32f, 32f);
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0f)).xyz;
    toLightVector = toLightVector - worldCoords.xyz;

    gl_Position = worldCoords;

}