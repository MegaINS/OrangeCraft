#version 330

in vec2 outTexCoord;
in vec3 mvPos;
in vec3 outColor;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 colour;
uniform int hasTexture;

void main()
{
    fragColor = vec4(colour, 1) ;
    fragColor *= vec4(outColor, 1);


    if ( hasTexture == 1 )
    {
        fragColor *=  texture(texture_sampler, outTexCoord);
    }


}