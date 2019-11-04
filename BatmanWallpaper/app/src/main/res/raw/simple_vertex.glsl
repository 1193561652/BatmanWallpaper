attribute vec4 a_Position;
attribute vec3 a_Normal;
uniform vec4 u_Color;

//varying vec4 v_Color;

uniform mat4 u_ProjectionMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ModelMatrix;
uniform int u_TextureTurn;
uniform mat4 u_TextureMatrix;

attribute vec2 a_TexturePosition;
varying vec2 v_TexturePosition;

varying vec3 v_Normal;

void main()
{
    vec4 texturePosition = vec4(a_TexturePosition, 0.0, 1.0);  //vec2(a_TexturePosition.s, a_TexturePosition.t);
    if(u_TextureTurn != 0)
    {
        texturePosition.y = 1.0 - texturePosition.y;
    }

    texturePosition = u_TextureMatrix * texturePosition;
    v_TexturePosition.xy = texturePosition.xy;

    vec4 postion = vec4(a_Position.x, a_Position.y, a_Position.z, 1.0);
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * postion;
    vec4 normal = u_ModelMatrix * vec4(a_Normal, 0.0);

    v_Normal = normalize(vec3(normal.x, normal.y, normal.z));
    //vec4 normal = vec4(a_Normal, 0.0);
    //float diffuse = max(-dot(normalize(vec3(normal.x, normal.y, normal.z)), vec3(0 ,0, -1)), 0.0);
    //v_Color = u_Color * diffuse;

    //gl_Position = a_Position;
}